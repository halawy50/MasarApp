package com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.masar.masatapp1.databinding.DialogProgressBinding

class ProgressDialog(context: Context) : AppCompatDialog(context) {
    private lateinit var binding: DialogProgressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DialogProgressBinding.inflate(layoutInflater)
        var view=binding.root
        setContentView(view)

    }
}