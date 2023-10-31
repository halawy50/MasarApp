package com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.masar.masatapp1.R
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.Driver_Results_Adapter
import com.masar.masatapp1.databinding.FragmentPageTwoBookStudentBinding
import com.masar.masatapp1.UI.Student.stateData.SateData
import com.masar.masatapp1.server

class PageTwo_Book_Fragment : Fragment() {
//
    lateinit var binding: FragmentPageTwoBookStudentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPageTwoBookStudentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllDeliveryApprove()
        binding.buttonPrevus.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonNext.setOnClickListener {
            if (SateData.selectedPosition !=-1){

                findNavController().navigate(R.id.action_pageTwo_Book_Fragment_to_pageThree_Book_Fragment)
            }else{
                Toast.makeText(requireContext(),"أختر السائق",Toast.LENGTH_LONG).show()
            }
        }
    }
    fun getAllDeliveryApprove() {

        var deliveryApproveAdapter =
        Driver_Results_Adapter(server.student.getDriverList(), requireContext())
        binding.recyclerDelivery.adapter = deliveryApproveAdapter

        }
    }
