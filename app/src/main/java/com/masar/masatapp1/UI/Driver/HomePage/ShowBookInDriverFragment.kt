package com.masar.masatapp1.UI.Driver.HomePage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.masar.masatapp1.UI.CurrentLocation
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.UI.Student.stateData.SateData
import com.masar.masatapp1.databinding.FragmentShowBookInDriverBinding
import com.masar.masatapp1.server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShowBookInDriverFragment : Fragment() {
    lateinit var binding: FragmentShowBookInDriverBinding
    var stateOrdr = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {    bundle ->
            stateOrdr=bundle.getInt("stateOrder")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowBookInDriverBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info_Book()

        binding.location.setOnClickListener {
            var intent = Intent(requireContext(),CurrentLocation::class.java)
            intent.putExtra("latitude",SateData.bookInfo.latitude)
            intent.putExtra("longitude",SateData.bookInfo.longitude)
            requireContext().startActivity(intent)
        }



        if (stateOrdr==2){
            binding.doneOrder.isGone = false
            binding.acceptOrder.isGone = true
            binding.rejectOrder.isGone = true
        }
        else if (stateOrdr==3||stateOrdr==0){
            binding.linearState.isGone = true
        }
        else if (stateOrdr==1){
            binding.doneOrder.isGone = true
            binding.acceptOrder.isGone = false
            binding.rejectOrder.isGone = false
        }
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.acceptOrder.setOnClickListener {
            var progressDialog =  ProgressDialog(requireContext())
            progressDialog.show()
            progressDialog.setCancelable(false)

            server.dtiver.acceptOrder(SateData.bookInfo.idBook,SateData.bookInfo.idDriver,SateData.bookInfo.counter,{
                if (it==true){
                    Snackbar.make(binding.root,"تم تحديث الحالة", Snackbar.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    progressDialog.dismiss()
                }else{
                    progressDialog.dismiss()
                }
            })
        }
        binding.doneOrder.setOnClickListener {
            var progressDialog =  ProgressDialog(requireContext())
            progressDialog.show()
            progressDialog.setCancelable(false)
            server.dtiver.doneOrder(SateData.bookInfo.idBook,SateData.bookInfo.idDriver,SateData.bookInfo.counter,{
                if (it==true){
                    Snackbar.make(binding.root,"تم تحديث الحالة", Snackbar.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    progressDialog.dismiss()
                }else{
                    progressDialog.dismiss()
                }
            })
        }
        binding.rejectOrder.setOnClickListener {
            var progressDialog =  ProgressDialog(requireContext())
            progressDialog.show()
            progressDialog.setCancelable(false)
            server.dtiver.rejectOrder(SateData.bookInfo.idBook,SateData.bookInfo.idDriver,SateData.bookInfo.counter,{
                if (it==true){
                    Snackbar.make(binding.root,"تم تحديث الحالة", Snackbar.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    progressDialog.dismiss()
                }else{
                    progressDialog.dismiss()
                }
            })
        }
    }

    private fun info_Book() {
//        var server = StudentServer()
        CoroutineScope(Dispatchers.IO).launch {
//           var bookInfo = server.getOneBook(bookID)
            withContext(Dispatchers.Main){
                binding.apply {
                    name.text = "${SateData.bookInfo.name_Person}"
                    nameDriver.text = "${SateData.bookInfo.nameDriver}"
                    phoneNumber.text = "${SateData.bookInfo.phoneNumber}"
                    subCity.text = "${SateData.bookInfo.round}"
                    subscribe.text = "${SateData.bookInfo.subscribe}"
                    city.text = "${SateData.bookInfo.city}"
                    counterSeat.text = "${SateData.bookInfo.counter}"
                    goBack.text = "${SateData.bookInfo.go_back}"
                    typeCar.text = "${SateData.bookInfo.typeCar}"
                    addressFrom.text = "${SateData.bookInfo.address_from}"
                    addressTo.text = "${SateData.bookInfo.address_to}"
                    if (SateData.bookInfo.stateBook==1){
                        stateOrder.setTextColor(Color.parseColor("#FF7A00"))
                        stateOrder.text = "في الانتظار"
                    }
                    else if (SateData.bookInfo.stateBook==0){
                        stateOrder.text = "طلب مرفوض"
                    }
                    else if (SateData.bookInfo.stateBook==2){
                        stateOrder.text = "يعمل حاليا"
                    }
                    else if (SateData.bookInfo.stateBook==3){
                        stateOrder.text = "انتهت الرحلة"
                    }
                }
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

}
