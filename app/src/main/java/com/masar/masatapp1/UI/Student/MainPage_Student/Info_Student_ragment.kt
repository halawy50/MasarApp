package com.masar.masatapp1.UI.Student.MainPage_Student
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.masar.masatapp1.Server.StudentServer
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.databinding.FragmentInfoStudentRagmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Info_Student_ragment : Fragment() {
    lateinit var binding : FragmentInfoStudentRagmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoStudentRagmentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
        var token_Student = sharedPref.getString("Token","").toString()
        super.onViewCreated(view, savedInstanceState)
        var server = StudentServer()
        CoroutineScope(Dispatchers.IO).launch {
            var student = server.getDataStudent(token_Student)
            withContext(Dispatchers.Main){
                binding.apply {
                    nameStudent.setText("${student.nameStudent}")
                    ageStudent.setText("${student.age}")
                    schoolName.setText("${student.SchoolName}")
                    buttonUpdateInfoStudent.setOnClickListener {
                        if (nameStudent.text.isNullOrEmpty()){
                            nameStudent.setError("الاسم فارغ")
                            return@setOnClickListener
                        }
                        if (schoolName.text.isNullOrEmpty()){
                            schoolName.setError("يجب ادخال اسم المدرسة")
                            return@setOnClickListener
                        }
                        if (ageStudent.text.isNullOrEmpty()){
                            ageStudent.setError("يجب ادخال العمر")
                            return@setOnClickListener
                        }

                        var progressDialog =  ProgressDialog(requireContext())
                        progressDialog.show()
                        server.updateDataStudent(nameStudent.text.toString(),ageStudent.text.toString(),
                            schoolName.text.toString(),token_Student,{
                                progressDialog.dismiss()

                                CoroutineScope(Dispatchers.Main).launch {
                                    Snackbar.make(binding.root,
                                        "تم تحديث البيانات بنجاح"
                                        ,Snackbar.LENGTH_LONG).show()
                                    findNavController().popBackStack()
                                }

                            })
                    }
                }

            }
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()

        }
    }

}