package com.masar.masatapp1.UI.Student.Regiter_Student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.masar.masatapp1.R
import com.masar.masatapp1.Server.StudentServer
import com.masar.masatapp1.databinding.FragmentSignUpStudentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUp_Student_Fragment : Fragment() {
    lateinit var binding: FragmentSignUpStudentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpStudentBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener {
           findNavController().navigate(R.id.action_signUp_Student_Fragment_to_login_Student_Fragment)
        }

        binding.buttonSignUP.setOnClickListener {
            SignUpStudent()
        }
    }

    private fun SignUpStudent(){
        var name = binding.nameSignUP.text.toString()
        var email = binding.emailSignUP.text.toString()
        var password = binding.passwordSignUP.text.toString()

        if (name.isNullOrEmpty()){
            binding.nameSignUP.setError("الاسم فارغ")
            return
        }
        if (email.isNullOrEmpty()){
            binding.emailSignUP.setError("البريد الايكتروني فارغ")
            return
        }
         if(password.length<8){
            binding.passwordSignUP.setError("الرقم السري ضعيف")
            return
        }
        binding.progressbarSignUP.isVisible=true
        binding.textSignUP.isGone=true
        binding.buttonSignUP.backgroundTintList=getResources().getColorStateList(R.color.gray)

        var serverStudent= StudentServer()
        serverStudent.signUpStudent(requireContext(),name,email,password,{
            CoroutineScope(Dispatchers.Main).launch {
                if (it==true){
                    Snackbar.make(binding.root,"تم انشاء الحساب بنجاح",Snackbar.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_signUp_Student_Fragment_to_homePage_Student_Fragment)
                    binding.progressbarSignUP.isVisible=false
                    binding.textSignUP.isGone=false
                    binding.buttonSignUP.backgroundTintList=getResources().getColorStateList(R.color.orange)
                }
                else{
                    Snackbar.make(binding.root,"يوجد خطأ ما",Snackbar.LENGTH_LONG).show()
                    binding.progressbarSignUP.isVisible=false
                    binding.textSignUP.isGone=false
                    binding.buttonSignUP.backgroundTintList=getResources().getColorStateList(R.color.orange)
                }
            }

        })
    }


}
