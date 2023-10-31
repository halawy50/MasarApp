package com.masar.masatapp1.UI

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.masar.masatapp1.R

class SplashScreen_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        // Set the status bar color to transparent
        activity?.window?.statusBarColor = Color.TRANSPARENT
        Handler().postDelayed({
            val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
            var token = sharedPref.getString("Token","").toString()
            var who = sharedPref.getInt("who",0)
            if (token.isNullOrEmpty()||who==0){
                findNavController().navigate(R.id.action_splashScreen_Fragment_to_firstPage_Fragment)
            }else if (token.isNotEmpty() && who==1){
                findNavController().navigate(R.id.action_splashScreen_Fragment_to_homePage_Student_Fragment)
            }
            else if (token.isNotEmpty() && who==2){
                findNavController().navigate(R.id.action_splashScreen_Fragment_to_homeDriverFragment2)
            }
            else if (token.isNotEmpty() && who==3){
                findNavController().navigate(R.id.action_splashScreen_Fragment_to_homeCompanyDeiverFragment)
            }
        },2000)


    }


}