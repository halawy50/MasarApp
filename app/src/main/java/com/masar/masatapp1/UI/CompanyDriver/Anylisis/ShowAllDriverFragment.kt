package com.masar.masatapp1.UI.CompanyDriver.Anylisis

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.masar.masatapp1.UI.CompanyDriver.Anylisis.Adapter.DriverAdapter
import com.masar.masatapp1.server
import com.masar.masatapp1.databinding.FragmentShowAllDriverBinding
import com.masar.masatapp1.server.cityCompany
import com.masar.masatapp1.server.namyCompany
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowAllDriverFragment : Fragment() {
    lateinit var binding : FragmentShowAllDriverBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowAllDriverBinding.inflate(layoutInflater,container,false)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        requireActivity().window.statusBarColor = Color.parseColor("#CCCCCC")
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cityCompany.text = cityCompany
        binding.nameCompany.text = namyCompany

            binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                var driverAdapter = DriverAdapter(server.company.getAllDriverList())
                Log.e("ListDriver",server.company.getAllDriverList().toString())
                binding.recyclerDriver.adapter = driverAdapter

            }
        }
    }
}