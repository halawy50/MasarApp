package com.masar.masatapp1.UI.Driver.Login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.masar.masatapp1.R
import com.masar.masatapp1.databinding.FragmentLoginDriverBinding
import com.masar.masatapp1.server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginDriverFragment : Fragment() {

    lateinit var bindind : FragmentLoginDriverBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindind = FragmentLoginDriverBinding.inflate(layoutInflater,container,false)
//
        // Inflate the layout for this fragment
        return bindind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindind.buttonSignIn.setOnClickListener {
            signInDriver()
        }
        bindind.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun signInDriver(){
        var email = bindind.etUserEmail.text.toString()
        var password = bindind.etPassword.text.toString()
        if (email.isNullOrEmpty()){
            bindind.etUserEmail.setError("البريد الاليكتروني فارغ")
            return
        }
        if (password.isNullOrEmpty()){
            bindind.etUserEmail.setError("كلمة المرور فارغة")
            return
        }
        else{
            bindind.progressbarSignin.isVisible=true
            bindind.textSignin.isGone=true
            bindind.buttonSignIn.backgroundTintList=getResources().getColorStateList(R.color.gray)
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener {
                var token_Driver = Firebase.auth.uid.toString()
                FirebaseFirestore.getInstance().collection("Drivers").document(token_Driver)
                    .get().addOnSuccessListener {
                        val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("Token",token_Driver)
                        editor.putInt("who",2)//Drivers
                        editor.apply()
                        getAllDataServer()
                        findNavController().navigate(R.id.action_loginDriverFragment_to_homeDriverFragment)
                        bindind.progressbarSignin.isVisible=false
                        bindind.textSignin.isGone=false
                        bindind.buttonSignIn.backgroundTintList=getResources().getColorStateList(R.color.orange)
                    }.addOnFailureListener{
                        Toast.makeText(requireContext(),"يوجد مشكلة في البريد الالكتروني او كلمة المرور",
                            Toast.LENGTH_LONG).show()
                        bindind.progressbarSignin.isVisible=false
                        bindind.textSignin.isGone=false
                        bindind.buttonSignIn.backgroundTintList=getResources().getColorStateList(R.color.orange)
                    }
                    }.addOnFailureListener{
                Toast.makeText(requireContext(),"يوجد مشكلة في البريد الالكتروني او كلمة المرور",
                    Toast.LENGTH_LONG).show()
                bindind.progressbarSignin.isVisible=false
                bindind.textSignin.isGone=false
                bindind.buttonSignIn.backgroundTintList=getResources().getColorStateList(R.color.orange)
            }

        }


    }

    @SuppressLint("SuspiciousIndentation")
    fun getAllDataServer(){
        val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
        var token = sharedPref.getString("Token","").toString()
        CoroutineScope(Dispatchers.IO).launch {
             server.dtiver.getAllBook(token.trim())
            }
        }

}
