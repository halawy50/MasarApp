package com.masar.masatapp1.UI.Student.MainPage_Student

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.masar.masatapp1.R
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.databinding.FragmentSettingStudentBinding


class Setting_Student_Fragment : Fragment() {

    lateinit var binding : FragmentSettingStudentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingStudentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignOut.setOnClickListener {
            var progressDialog =  ProgressDialog(requireContext())
            progressDialog.show()
            FirebaseAuth.getInstance().signOut()
            val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("Token","")
            editor.putInt("who",0)//Student
            editor.apply()

            findNavController().navigate(R.id.action_setting_Student_Fragment_to_firstPage_Fragment)
            progressDialog.dismiss()
        }
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.linearPolicy.setOnClickListener {
            findNavController().navigate(R.id.action_setting_Student_Fragment_to_policyFragment)
        }
        binding.lineardeleteAccount.setOnClickListener {
            DialogDeleteAccount(requireContext(),requireActivity()).show()
        }
    }

}