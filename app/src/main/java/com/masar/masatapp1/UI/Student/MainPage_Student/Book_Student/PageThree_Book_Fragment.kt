package com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.masar.masatapp1.Data.BookInfo_Data
import com.masar.masatapp1.R
import com.masar.masatapp1.Server.StudentServer
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.databinding.FragmentPageThreeBookStudentBinding
import com.masar.masatapp1.UI.Student.stateData.SateData
import com.masar.masatapp1.UI.Student.stateData.SateData.name_Person
import com.masar.masatapp1.UI.Student.stateData.SateData.city
import com.masar.masatapp1.UI.Student.stateData.SateData.phoneNumber
import com.masar.masatapp1.UI.Student.stateData.SateData.round
import com.masar.masatapp1.UI.Student.stateData.SateData.subscribe
import com.masar.masatapp1.UI.Student.stateData.SateData.address_map
import com.masar.masatapp1.UI.Student.stateData.SateData.address_to
import com.masar.masatapp1.UI.Student.stateData.SateData.counter
import com.masar.masatapp1.UI.Student.stateData.SateData.idComany
import com.masar.masatapp1.UI.Student.stateData.SateData.idDelivery
import com.masar.masatapp1.UI.Student.stateData.SateData.latitude
import com.masar.masatapp1.UI.Student.stateData.SateData.longitude
import com.masar.masatapp1.UI.Student.stateData.SateData.nameComany
import com.masar.masatapp1.UI.Student.stateData.SateData.nameDelivery
import com.masar.masatapp1.UI.Student.stateData.SateData.selectedPosition
import com.masar.masatapp1.UI.Student.stateData.SateData.typeCate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID


class PageThree_Book_Fragment : Fragment() {

    lateinit var binding : FragmentPageThreeBookStudentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPageThreeBookStudentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name.text = name_Person
        binding.phoneNumber.text = phoneNumber
        binding.city.text = city
        binding.subCity.text = round
        binding.goBack.text = SateData.go_back
        binding.typeCar.text = SateData.typeCate
        binding.addressFrom.text = address_map
        binding.addressTo.text = address_to
        binding.nameDriver.text = SateData.nameDelivery
        binding.subscribe.text = subscribe
        binding.counterSeat.text = "${counter}"

        binding.buttonPrevus.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonDone.setOnClickListener {
            val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
            var token_Student = sharedPref.getString("Token","").toString()
            var who = sharedPref.getInt("who",0).toInt()

            if (who==1){
                val autoID = UUID.randomUUID().toString()
                var data_Book = BookInfo_Data(
                    autoID,nameDelivery, idDelivery, name_Person, typeCate,token_Student,
                    city,
                    round,phoneNumber, SateData.go_back, subscribe, address_map,
                    address_to,1,
                    counter,
                    idComany, nameComany,SateData.latitude,SateData.longitude
                )
                var server = StudentServer()
                var progressDialog =  ProgressDialog(requireContext())
                progressDialog.show()
                progressDialog.setCancelable(false)

                server.bookStudent(data_Book,{
                    if (it==true){

                        Snackbar.make(binding.root,"تم الحجز بنجاح , سنرد عليك قريبا", Snackbar.LENGTH_LONG).show()
                        counter =1
                        selectedPosition = -1
                        nameDelivery = ""
                        idDelivery = ""
                        name_Person =""
                        typeCate = ""
                        city = ""
                        round = "" //المدرية
                        phoneNumber = ""
                        SateData.go_back = ""
                        subscribe = ""
                        address_map = ""
                        address_to=""
                        latitude = 0.0
                        longitude = 0.0
                        getallList()
                        progressDialog.dismiss()


                    }
                    else{
                        Snackbar.make(binding.root,"حدث مشكلة ما", Snackbar.LENGTH_LONG).show()
                        progressDialog.dismiss()

                    }
                })

            }

        }

    }
    fun getallList() {
        val sharedPref = requireActivity().getSharedPreferences("Customer", Context.MODE_PRIVATE)
        var token = sharedPref.getString("Token", "").toString()
        if (token != null) {
            CoroutineScope(Dispatchers.IO).launch {
                var server = StudentServer()
                // Start the async task to execute server.getAllBook_Activty(token)
                val result = async {
                    server.getAllBook_Activty(token)
                }

                // Wait for the async task to complete and get the result
                result.await()

                // After the server operation completes, navigate to the desired fragment
                withContext(Dispatchers.Main) {
                    findNavController().navigate(R.id.action_pageThree_Book_Fragment_to_all_Book_Student_Fragment)

                }
            }
        }
    }

}