package com.masar.masatapp1.UI.Driver.HomePage

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.masar.masatapp1.Data.BookInfo_Data
import com.masar.masatapp1.R
import com.masar.masatapp1.UI.Driver.HomePage.Adapter.Adapter_AllBook_Driver
import com.masar.masatapp1.UI.Student.MainPage_Student.DialogDeleteAccount
import com.masar.masatapp1.databinding.FragmentHomeDriverBinding
import com.masar.masatapp1.server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class HomeDriverFragment : Fragment() {

    lateinit var binding : FragmentHomeDriverBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        requireActivity().window.statusBarColor = Color.parseColor("#CCCCCC")
        binding = FragmentHomeDriverBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
        var token = sharedPref.getString("Token","").toString()
        getAllBook()

        server.dtiver.getCounterSeats(token,{numberOfSeats , seatsApprove ->
            binding.counterSeats.setText("${seatsApprove}")
            binding.allSeats.setText("${numberOfSeats}")
        })

        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("Token","")
            editor.putInt("who",0)//Student
            editor.apply()
            findNavController().navigate(R.id.action_homeDriverFragment_to_firstPage_Fragment)
        }
    }
    fun getAllBook(){
        val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
        var token = sharedPref.getString("Token","").toString()
        CoroutineScope(Dispatchers.IO).launch {
            try{
                var REF_ListBook = FirebaseFirestore.getInstance().collection("Book")
                    .whereEqualTo("idDriver", token)
                var listBook = REF_ListBook.get().await().toObjects(BookInfo_Data::class.java)
                Log.e("ListBook",token.toString())

                withContext(Dispatchers.Main){
                    binding.bookRecycler.adapter = Adapter_AllBook_Driver(listBook,requireContext())
                }
            }catch (e:Exception){
            }
        }
    }





}