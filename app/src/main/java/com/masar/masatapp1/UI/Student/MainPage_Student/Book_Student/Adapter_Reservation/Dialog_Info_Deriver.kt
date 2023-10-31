package com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.masar.masatapp1.Data.DriverInfo
import com.masar.masatapp1.databinding.DialogInfoDeriverBinding

class Dialog_Info_Deriver (context: Context, private var InfoData : DriverInfo) : AppCompatDialog(context) {

    private lateinit var binding: DialogInfoDeriverBinding
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogInfoDeriverBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        binding.apply {
            name.text = "${InfoData.name}"
            numberOfVechile.text = "${InfoData.numberOfVechile}"
            typeOfLicense.text = "${InfoData.typeOfLicense}"
            numberofLicenseTrasport.text = "${InfoData.LicenseNumberOfLandTransport}"
            typeCar.text = "${InfoData.typeOfVechile}"

        }

    }
}