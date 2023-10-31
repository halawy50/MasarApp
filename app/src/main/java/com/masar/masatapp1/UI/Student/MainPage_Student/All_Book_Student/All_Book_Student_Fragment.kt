package com.masar.masatapp1.UI.Student.MainPage_Student.All_Book_Student

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.masar.masatapp1.Server.StudentServer
import com.masar.masatapp1.UI.Student.MainPage_Student.All_Book_Student.Adapter.Adapter_AllBook
import com.masar.masatapp1.databinding.FragmentAllBookStudentBinding


class All_Book_Student_Fragment : Fragment() {

    lateinit var binding : FragmentAllBookStudentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBookStudentBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allBook()
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    fun allBook() {
        var server = StudentServer()
        if (server.getAllBook().isEmpty())
            Handler().postDelayed({
                binding.shimmerlist.isGone = true
                binding.emptyList.isGone = false
            },1000)

        else {
            binding.shimmerlist.isGone = true
            var adapter_AllBook = Adapter_AllBook(server.getAllBook(), requireContext())
            binding.allBookRecycler.adapter = adapter_AllBook
        }
    }

}