package com.masar.masatapp1.UI.Student.Regiter_Student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.databinding.FragmentForgetPasswordStudentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Forget_Password_Student_Fragment : Fragment() {

    lateinit var binding : FragmentForgetPasswordStudentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetPasswordStudentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonFaqStudent.setOnClickListener {
            forgetPassword()
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun forgetPassword(){
        var email = binding.emailForgetPassword.text.toString()
        if (email.isNullOrEmpty()){
            binding.emailForgetPassword.setError("برجاء ادخال البريد الاليكتروني")
            return
        }
        var progressDialog =  ProgressDialog(requireContext())
        progressDialog.show()
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
        CoroutineScope(Dispatchers.Main).launch {
            if(it.isSuccessful){
                Snackbar.make(
                    requireView(),
                    "تم ارسال الرمز علي البريد الخاص بك",
                    Snackbar.LENGTH_LONG)
                    .show()
                findNavController().popBackStack()
                progressDialog.dismiss()
            }else{
                Snackbar.make(
                    requireView(),
                    "هذا البريد غير متوفر",
                    Snackbar.LENGTH_LONG)
                    .show()
                progressDialog.dismiss()

            }
        }

        }
    }
}