package com.masar.masatapp1.UI.Student.MainPage_Student

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.google.firebase.auth.FirebaseAuth
import com.masar.masatapp1.UI.MainActivity
import com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation.ProgressDialog
import com.masar.masatapp1.databinding.DialogDeleteAccountBinding

class DialogDeleteAccount(context: Context,var activity: Activity) : AppCompatDialog(context) {
    private lateinit var binding: DialogDeleteAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DialogDeleteAccountBinding.inflate(layoutInflater)
        var view=binding.root
        setContentView(view)
        binding.deleteAccount.setOnClickListener {
            if (binding.email.text.toString().isEmpty()){
                binding.email.setError("مكان البريد فارغ")
                return@setOnClickListener
            }
            if (binding.password.text.toString().isEmpty()){
                binding.password.setError("كلمة السر فارغة")
                return@setOnClickListener
            }
            var email = binding.email.text.toString()
            var password = binding.password.text.toString()

            var progressDialog =  ProgressDialog(context)
            progressDialog.show()
            progressDialog.setCancelable(false)
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser?.delete()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(context,"تم حذف حسابك",Toast.LENGTH_LONG).show()
                            val sharedPref = context.getSharedPreferences("Customer", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putString("Token","")
                            editor.putInt("who",0)//Drivers
                            editor.apply()
                            activity.finish()
                            activity.startActivity(Intent(context,MainActivity::class.java))
                        } else {
                            progressDialog.dismiss()

                            Toast.makeText(context,"حدذث خطأ ما",Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
        binding.dismess.setOnClickListener {
          dismiss()
        }
    }

}