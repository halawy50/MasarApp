package com.masar.masatapp1.UI.CompanyDriver.Login_CompanyDriver

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.masar.masatapp1.R
import com.masar.masatapp1.server
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.databinding.FragmentLogInCompanyDriverBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogInCompanyDriverFragment : Fragment() {
    lateinit var binding : FragmentLogInCompanyDriverBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInCompanyDriverBinding.inflate(layoutInflater,container,false)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        requireActivity().window.statusBarColor = Color.parseColor("#CCCCCC")
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignIn.setOnClickListener {
            SignInCompany()
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    fun SignInCompany(){
        var email = binding.etUserEmail.text.toString()
        var password = binding.etPassword.text.toString()

        if (email.isNullOrEmpty()){
            binding.etUserEmail.setError("البريد الاليكتروني فارغ")
            return
        }
        if (password.isNullOrEmpty()){
            binding.etPassword.setError("كلمة المرور فارغة")
            return
        }
        else{
            var progressDialog =  ProgressDialog(requireContext())
            progressDialog.show()
            progressDialog.setCancelable(false)
            server.company.signInCompany(email,password,{
                if (it==true){
                    var token_Student = Firebase.auth.uid.toString()
                    val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("Token",token_Student)
                    editor.putInt("who",3)//Student
                    editor.apply()
                    getAllDataServer()
                    progressDialog.dismiss()
                    findNavController().navigate(R.id.action_logInCompanyDriverFragment_to_homeCompanyDeiverFragment)

                }else{
                    Toast.makeText(requireContext(),"يوجد خطا في البريد الاليكتروني او كلمة السر",Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            })
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getAllDataServer(){
        val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
        var token = sharedPref.getString("Token","").toString()
        CoroutineScope(Dispatchers.IO).launch {
            if (token!=null){
                server.company.getAllDriver(token)
            }
        }
    }
}