package com.masar.masatapp1.UI.Student.MainPage_Student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.databinding.FragmentFAQStudentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FAQ_Student_Fragment : Fragment() {

    lateinit var binding : FragmentFAQStudentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFAQStudentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonFaqStudent.setOnClickListener {
            setFAQ()
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
    private fun setFAQ(){
        var titleFAQ = binding.faqTitleStudent.text.toString()
        var FAQ = binding.faqStudent.text.toString()

        if (titleFAQ.isNullOrEmpty()){
            binding.faqTitleStudent.setError("ادخل عنوان الشكوي")
            return
        }
        if (FAQ.isNullOrEmpty()){
            binding.faqStudent.setError("ادخل الشكوي")
            return
        }
        var token = Firebase.auth.uid.toString()
        val faq = mapOf(
            "titleFAQ" to "${titleFAQ}",
            "FAQ" to "${FAQ}",
            "idStudent" to "${token}",
        )
        var progressDialog =  ProgressDialog(requireContext())
        progressDialog.show()
        FirebaseFirestore.getInstance().collection("FAQ").document().set(faq).addOnSuccessListener {
            CoroutineScope(Dispatchers.Main).launch {
                Snackbar.make(binding.root,"تم تقديم شكواك بنجاح", Snackbar.LENGTH_LONG).show()
                progressDialog.dismiss()

                findNavController().popBackStack()

            }
        }
    }
}