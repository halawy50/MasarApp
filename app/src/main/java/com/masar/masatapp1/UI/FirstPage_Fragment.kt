package com.masar.masatapp1.UI

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.masar.masatapp1.R
import com.masar.masatapp1.databinding.FragmentFirstPageBinding

class FirstPage_Fragment : Fragment() {

    lateinit var bindind : FragmentFirstPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindind = FragmentFirstPageBinding.inflate(layoutInflater,container,false)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        requireActivity().window.statusBarColor = Color.parseColor("#CCCCCC")
        // Inflate the layout for this fragment
        return bindind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindind.buttonStudent.setOnClickListener {
            findNavController().navigate(R.id.action_firstPage_Fragment_to_login_Student_Fragment)
        }
        bindind.companyAccount.setOnClickListener {
            findNavController().navigate(R.id.action_firstPage_Fragment_to_logInCompanyDriverFragment)
        }
        bindind.buttonDelivery.setOnClickListener {
            findNavController().navigate(R.id.action_firstPage_Fragment_to_loginDriverFragment)
        }
    }


}