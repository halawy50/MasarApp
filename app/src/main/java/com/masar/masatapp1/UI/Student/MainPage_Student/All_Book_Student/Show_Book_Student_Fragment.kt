package com.masar.masatapp1.UI.Student.MainPage_Student.All_Book_Student

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.masar.masatapp1.databinding.FragmentShowBookStudentBinding
import com.masar.masatapp1.UI.Student.stateData.SateData.bookInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Show_Book_Student_Fragment : Fragment() {
    lateinit var binding: FragmentShowBookStudentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowBookStudentBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info_Book()
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun info_Book() {
//        var server = StudentServer()
        CoroutineScope(Dispatchers.IO).launch {
//           var bookInfo = server.getOneBook(bookID)
            withContext(Dispatchers.Main){
                binding.apply {
                    name.text = "${bookInfo.name_Person}"
                    nameDriver.text = "${bookInfo.nameDriver}"
                    phoneNumber.text = "${bookInfo.phoneNumber}"
                    subCity.text = "${bookInfo.round}"
                    subscribe.text = "${bookInfo.subscribe}"
                    city.text = "${bookInfo.city}"
                    counterSeat.text = "${bookInfo.counter}"
                    goBack.text = "${bookInfo.go_back}"
                    typeCar.text = "${bookInfo.typeCar}"
                    addressFrom.text = "${bookInfo.address_from}"
                    addressTo.text = "${bookInfo.address_to}"
                    if (bookInfo.stateBook==1){
                        stateOrder.setTextColor(Color.parseColor("#FF7A00"))
                        stateOrder.text = "في الانتظار"
                    }
                    else if (bookInfo.stateBook==0){
                        stateOrder.text = "طلب مرفوض"
                    }
                    else if (bookInfo.stateBook==2){
                        stateOrder.text = "يعمل حاليا"
                    }
                    else if (bookInfo.stateBook==3){
                        stateOrder.text = "انتهت الرحلة"
                    }
                }
            }
        }

    }
}
