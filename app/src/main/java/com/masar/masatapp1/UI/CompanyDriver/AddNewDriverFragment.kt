package com.masar.masatapp1.UI.CompanyDriver

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.masar.masatapp1.R
import com.masar.masatapp1.databinding.FragmentAddNewDriverBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.masar.masatapp1.Data.DriverInfo
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewDriverFragment : Fragment() {
    lateinit var binding: FragmentAddNewDriverBinding
    private val PICK_IMAGE = 102
    private var  uri: Uri? = null
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                selectImage()
            } else {
                // Handle the case where permission is denied
            }
        }

    // Initialize Firebase Storage
    private val storage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewDriverBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var car = arrayOf("سيارة ركوب صغيرة","باص متوسط لنقل الركاب","حافلة ركاب كبيرة")
        //Car Spinner
        var adapterGo_Car = ArrayAdapter(requireContext(), R.layout.item_drom_down_city, car)
        binding.typeOfVechile.setAdapter(adapterGo_Car)
        binding.typeOfVechile.setOnItemClickListener { adapterView, view, i, l ->
//            SateData.typeCate = binding.typeOfVechile.text.toString()
        }

        binding.cardImage.setOnClickListener {
            requestStoragePermission()
        }

        binding.buttonSubmit.setOnClickListener {
            addNewDelivery()
        }
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            // Permission already granted, select the image
            selectImage()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.data!!
                if (uri != null) {
                    try {
                        val inputStream = requireContext().contentResolver.openInputStream(uri!!)
                        val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
                        binding.photoDriver.setImageBitmap(bitmap)
                        // Upload the image to Firebase Storage and store its URL in Firestore
                    } catch (e: Exception) {
                        // Handle exceptions
                    }
                }
            }
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}_${imageUri.lastPathSegment}")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image upload successful, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Store the download URL in Firestore
                    this.uri = uri

                }
            }
            .addOnFailureListener { e ->
                // Handle the failure
            }
    }
//
//    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
//        val imageRef = storageRef.child("images/${System.currentTimeMillis()}_${imageUri.lastPathSegment}")
//
//        imageRef.putFile(imageUri)
//            .addOnSuccessListener { taskSnapshot ->
//                // Image upload successful, get the download URL
//                imageRef.downloadUrl.addOnSuccessListener { uri ->
//                    // Store the download URL in Firestore or use it as needed
//                    this.uri = uri
//                }
//            }
//            .addOnFailureListener { e ->
//                // Handle the failure
//            }
//    }



    fun addNewDelivery(){
        var name_driver = binding.nameDriver.text.toString()
        var nationalID = binding.nationalID.text.toString()
        var typeOfLicence = binding.typeOfLicence.text.toString()
        var typeOfVechile = binding.typeOfVechile.text.toString()
        var numberOfVechile = binding.numberOfVechile.text.toString()
        var numberOfSeats = binding.numberOfSeats.text.toString()
        var licenceNumberTransportAuthority = binding.licenceNumberTransportAuthority.text.toString()
        var etUserEmail = binding.etUserEmail.text.toString()
        var etPassword = binding.etPassword.text.toString()
        var vechileLicence = binding.vechileLicense.text.toString()

        if (uri==null){
            Snackbar.make(binding.root,"لم يتم اضافة صورة للسائق", Snackbar.LENGTH_LONG).show()
            return
        }
        if (name_driver.isEmpty()){
            binding.nameDriver.setError("المكان فارغ")
            return
        }
        if (nationalID.isEmpty()){
            binding.nationalID.setError("المكان فارغ")
            return
        }
        if (typeOfLicence.isEmpty()){
            binding.typeOfLicence.setError("المكان فارغ")
            return
        }
        if (typeOfVechile.isEmpty()){
            binding.typeOfVechile.setError("المكان فارغ")
            return
        }
        if (numberOfVechile.isEmpty()){
            binding.numberOfVechile.setError("المكان فارغ")
            return
        }
        if (numberOfSeats.isEmpty()){
            binding.numberOfSeats.setError("المكان فارغ")
            return
        }
        if (vechileLicence.isEmpty()){
            binding.vechileLicense.setError("المكان فارغ")
            return
        }

//        if (licenceNumberTransportAuthority.isEmpty()){
//            binding.licenceNumberTransportAuthority.setError("المكان فارغ")
//            return
//        }
        if (etUserEmail.isEmpty()){
            binding.etUserEmail.setError("المكان فارغ")
            return
        }
        if (etPassword.isEmpty()){
            binding.etPassword.setError("المكان فارغ")
            return
        }

        else{
            var progressDialog =  ProgressDialog(requireContext())
            progressDialog.show()
            progressDialog.setCancelable(false)

            val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
            var tokenCompany = sharedPref.getString("Token","").toString()

            FirebaseFirestore.getInstance().collection("Company").document(tokenCompany)
                .get().addOnSuccessListener {
                   var nameCompany =  it.getString("name_Company").toString()
                   var idCompany = it.getString("idComapny").toString()

                    uploadImageToFirebaseStorage(uri!!)
                    server.company.checkIfEmailIsRegistered(etUserEmail,{
                        if (it==true){
                            progressDialog.dismiss()
                            Snackbar.make(binding.root,"هذا الحساب مستخدم", Snackbar.LENGTH_LONG).show()
                        }else{
                            var dataDelivery = DriverInfo("",name_driver,nationalID,typeOfLicence,typeOfVechile
                                ,numberOfVechile,vechileLicence,numberOfSeats.toInt(),numberOfSeats.toInt() ,licenceNumberTransportAuthority,uri.toString(),
                                true,idCompany,nameCompany,etUserEmail, etPassword)
                            server.company.signUpDelivery(requireContext(),dataDelivery,{
                                if(it==true){
                                    progressDialog.dismiss()
                                    Snackbar.make(binding.root,"تم اضافة الحساب بنجاح", Snackbar.LENGTH_LONG).show()
                                    findNavController().popBackStack()
                                    getAllDriver()
                                }else{
                                    progressDialog.dismiss()
                                    Snackbar.make(binding.root,"يوجد مشكلة ما يرجي اعادة المحاولة لاحقا", Snackbar.LENGTH_LONG).show()
                                }
                            })
                        }

                    })
                }


        }
    }

    fun getAllDriver(){
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
            var token = sharedPref.getString("Token","").toString()
            server.company.getAllDriver(token)
        }
    }

}
