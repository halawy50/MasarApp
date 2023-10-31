package com.masar.masatapp1.UI.Student.Regiter_Student

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.masar.masatapp1.R
import com.masar.masatapp1.Server.StudentServer
import com.masar.masatapp1.databinding.FragmentLoginStudentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Login_Student_Fragment : Fragment() {

    lateinit var bindind : FragmentLoginStudentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindind = FragmentLoginStudentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return bindind.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindind.buttonSignUP.setOnClickListener {
            findNavController().navigate(R.id.action_login_Student_Fragment_to_signUp_Student_Fragment)
        }
        bindind.buttonSignIn.setOnClickListener {
            signInStudent()
        }
        bindind.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_Student_Fragment_to_forget_Password_Student_Fragment)
        }
    }
    private fun signInStudent(){
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
                var token_Student = Firebase.auth.uid.toString()
                val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("Token",token_Student)
                editor.putInt("who",1)//Student
                editor.apply()
                getallList()
                findNavController().navigate(R.id.action_login_Student_Fragment_to_homePage_Student_Fragment)
                bindind.progressbarSignin.isVisible=false
                bindind.textSignin.isGone=false
                bindind.buttonSignIn.backgroundTintList=getResources().getColorStateList(R.color.orange)

            }.addOnFailureListener{
                Toast.makeText(requireContext(),"يوجد مشكلة في البريد الالكتروني او كلمة المرور",Toast.LENGTH_LONG).show()
                bindind.progressbarSignin.isVisible=false
                bindind.textSignin.isGone=false
                bindind.buttonSignIn.backgroundTintList=getResources().getColorStateList(R.color.orange)

            }
        }


    }
    fun getallList(){
        val sharedPref = requireActivity().getSharedPreferences("Customer", Context.MODE_PRIVATE)
        var token = sharedPref.getString("Token","").toString()
        if (token!=null){
            CoroutineScope(Dispatchers.IO).launch {
                var server = StudentServer()
                server.getAllBook_Activty(token)
            }
        }
    }


}