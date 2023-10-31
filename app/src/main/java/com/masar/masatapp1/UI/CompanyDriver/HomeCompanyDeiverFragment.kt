package com.masar.masatapp1.UI.CompanyDriver

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.masar.masatapp1.R
import com.masar.masatapp1.UI.Student.MainPage_Student.DialogDeleteAccount
import com.masar.masatapp1.databinding.FragmentHomeCompanyDeiverBinding

class HomeCompanyDeiverFragment : Fragment() {

    lateinit var binding : FragmentHomeCompanyDeiverBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        requireActivity().window.statusBarColor = Color.parseColor("#CCCCCC")
        binding = FragmentHomeCompanyDeiverBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNewDriver.setOnClickListener {
            findNavController().navigate(R.id.action_homeCompanyDeiverFragment_to_addNewDriverFragment)
        }

        binding.anylisis.setOnClickListener {
            findNavController().navigate(R.id.action_homeCompanyDeiverFragment_to_showAllDriverFragment)
        }
        binding.logoutCompany.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("Token","")
            editor.putInt("who",0)//Student
            editor.apply()
            findNavController().navigate(R.id.action_homeCompanyDeiverFragment_to_firstPage_Fragment)
        }
    }


}