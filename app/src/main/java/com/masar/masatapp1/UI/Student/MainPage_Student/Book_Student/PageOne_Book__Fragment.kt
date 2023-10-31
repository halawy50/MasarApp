package com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import com.masar.map.MapActivity
import com.masar.masatapp1.R
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.databinding.FragmentPage1OneBookStudentBinding
import com.masar.masatapp1.UI.Student.stateData.SateData
import com.masar.masatapp1.UI.Student.stateData.SateData.name_Person
import com.masar.masatapp1.UI.Student.stateData.SateData.city
import com.masar.masatapp1.UI.Student.stateData.SateData.phoneNumber
import com.masar.masatapp1.UI.Student.stateData.SateData.round
import com.masar.masatapp1.UI.Student.stateData.SateData.address_map
import com.masar.masatapp1.UI.Student.stateData.SateData.address_to
import com.masar.masatapp1.UI.Student.stateData.SateData.typeCate
import com.masar.masatapp1.server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PageOne_Book__Fragment : Fragment() {

    lateinit var binding : FragmentPage1OneBookStudentBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPage1OneBookStudentBinding.inflate(layoutInflater,container,false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        if (binding.from.text.toString().length>0){
            binding.from.isGone = false
        }
        binding.buttonMap.setOnClickListener {
            info_Book()
            if (!hasLocationPermission()) {
                requestLocationPermission()
            } else {
                requireContext().startActivity(Intent(requireContext(),MapActivity::class.java))
            }
        }
        city()
    }

    fun info_Book(){
        binding.apply {
            name_Person = nameReservation.text.toString()
            phoneNumber = phonNumber.text.toString()
            round = subCity.text.toString()
            address_map = from.text.toString()
           address_to = to.text.toString()

        }

    }

    fun city(){
        val values = arrayOf(
            "العاصمة",
            "الزرقاء",
            "السلط",
            "مادبا",
            "اربد",
            "عجلون",
            "جرش",
            "المفرق",
            "الكرك",
            "الطفيلة",
            "معان",
            "العقبة",
        )

        var go_back = arrayOf("ذهاب فقط","عودة فقط","ذهاب و عودة")
        var subscribe = arrayOf("اسبوعي","شهري","فصل دراسي")
        var car = arrayOf("سيارة ركوب صغيرة","باص متوسط لنقل الركاب","حافلة ركاب كبيرة")

        //Subscribe Spinner
        var adapterGo_Subscribe = ArrayAdapter(requireContext(), R.layout.item_drom_down_city, subscribe)
        binding.autoCompeleteSubscribe.setAdapter(adapterGo_Subscribe)
        binding.autoCompeleteSubscribe.setOnItemClickListener { adapterView, view, i, l ->
            SateData.subscribe = binding.autoCompeleteSubscribe.text.toString()
        }

        //Car Spinner
        var adapterGo_Car = ArrayAdapter(requireContext(), R.layout.item_drom_down_city, car)
        binding.autoCompeleteCar.setAdapter(adapterGo_Car)
        binding.autoCompeleteCar.setOnItemClickListener { adapterView, view, i, l ->
            SateData.typeCate = binding.autoCompeleteCar.text.toString()
        }

        //Go_Back Spinner
        var adapterGo_Back = ArrayAdapter(requireContext(), R.layout.item_drom_down_city, go_back)
        binding.autoCompeleteGoBack.setAdapter(adapterGo_Back)
        binding.autoCompeleteGoBack.setOnItemClickListener { adapterView, view, i, l ->
            SateData.go_back = binding.autoCompeleteGoBack.text.toString()
        }

        //City Spinner
        var adapterCity = ArrayAdapter(requireContext(), R.layout.item_drom_down_city, values)
        binding.autoCompeleteCity.setAdapter(adapterCity)
        binding.autoCompeleteCity.setOnItemClickListener { adapterView, view, i, l ->
            SateData.city = binding.autoCompeleteCity.text.toString()
        }

        binding.buttonNext.setOnClickListener {
            check()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.nameReservation.setText(name_Person)
        binding.phonNumber.setText(phoneNumber)
        binding.subCity.setText(round)
        binding.from.setText(address_map)
        binding.to.setText(address_to)
    }

    private fun check(){

        if (binding.nameReservation.text.isNullOrEmpty()){
            Snackbar.make(
                binding.root,
                "ادخل الاسم من فضلك",
                Snackbar.LENGTH_LONG
            )
                .show()
            return

        }
        if (binding.phonNumber.text.isNullOrEmpty()){
            Snackbar.make(
                binding.root,
                "ادخل رقم الهاتف",
                Snackbar.LENGTH_LONG
            )
                .show()
            return

        }
        if (binding.autoCompeleteSubscribe.text.isNullOrEmpty()){
            Snackbar.make(
                binding.root,
                "اختر نوع الاشتراك",
                Snackbar.LENGTH_LONG
            )
                .show()
            return

        }
        if (binding.subCity.text.isNullOrEmpty()){
            Snackbar.make(
                binding.root,
                "ادخل اسم المدرية التابع لها",
                Snackbar.LENGTH_LONG
            )
                .show()
            return

        }
        if (binding.autoCompeleteGoBack.text.isNullOrEmpty()){
            Snackbar.make(
                binding.root,
                "اختر مواعيد الذهاب والعودة",
                Snackbar.LENGTH_LONG
            )
                .show()
            return

        }
        if (binding.autoCompeleteCar.text.isNullOrEmpty()){
            Snackbar.make(
                binding.root,
                "اختر نوع المركبة",
                Snackbar.LENGTH_LONG
            )
                .show()
            return
        }
        if (binding.from.text.isNullOrEmpty()){
            Snackbar.make(
                binding.root,
                "ادخل عنوان الانطلاق",
                Snackbar.LENGTH_LONG
            )
                .show()
            return

        }
        if (binding.to.text.isNullOrEmpty()){
            Snackbar.make(
                binding.root,
                "ادخل عنوان المدرسة",
                Snackbar.LENGTH_LONG
            )
                .show()
            return

        }

        else{
            var progressDialog = ProgressDialog(requireContext())
            progressDialog.show()
            progressDialog.setCancelable(false)
            info_Book()
            server.student.getDriver(city, typeCate) { isSuccess ->
                CoroutineScope(Dispatchers.Main).launch {
                    progressDialog.dismiss() // Dismiss the progress dialog when the data retrieval is completed.
                    if (isSuccess==true) {
                        findNavController().navigate(R.id.action_pageOne_Book__Fragment_to_pageTwo_Book_Fragment)
                    } else {
                        Snackbar.make(binding.root, "لا يتوفر حاليا أي سائق جرب مرة اخري", Snackbar.LENGTH_LONG).show()
                    }
                }
            }


        }
    }


    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            locationPermissionCode
        )
    }

    override fun onResume() {
        super.onResume()
        binding.nameReservation.setText(name_Person)
        binding.phonNumber.setText(phoneNumber)
        binding.subCity.setText(round)
        binding.from.setText(address_map)
        binding.to.setText(address_to)
        binding.from.setText("${SateData.address_map}")
        if (binding.from.text.toString().length>0){
            binding.from.isGone = false
        }

    }

}