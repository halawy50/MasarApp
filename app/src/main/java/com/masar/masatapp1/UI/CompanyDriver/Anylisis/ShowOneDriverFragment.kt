package com.masar.masatapp1.UI.CompanyDriver.Anylisis

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.masar.masatapp1.Data.BookInfo_Data
import com.masar.masatapp1.R
import com.masar.masatapp1.server
import com.masar.masatapp1.server.driverInfo
import com.masar.masatapp1.databinding.FragmentShowOneDriverBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ShowOneDriverFragment : Fragment() {

    lateinit var binding : FragmentShowOneDriverBinding
    var idDriver = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {    bundle ->
            idDriver= bundle.getString("idDriver").toString()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        // Set the status bar color to transparent
        activity?.window?.statusBarColor = Color.TRANSPARENT

        binding = FragmentShowOneDriverBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        anylisisDriver()
        var stateSpinnerList = arrayOf("يعمل حاليا","تم حظره")
        //Subscribe Spinner
        var adapterGo_Subscribe = ArrayAdapter(requireContext(), R.layout.item_drom_down_city, stateSpinnerList)
        binding.stateCompanyChange.setAdapter(adapterGo_Subscribe)
        binding.stateCompanyChange.setOnItemClickListener { adapterView, view, i, l ->
            var state = binding.stateCompanyChange.text.toString()
            val stateAccount = mapOf("stateAccount" to (state == "يعمل حاليا"))

// Assuming you have the idDriver and state values defined

            val db = FirebaseFirestore.getInstance()
            val driversCollection = db.collection("Drivers")

// Query the document based on the "idDriver" field
            driversCollection.whereEqualTo("idDriver", idDriver)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        // Update the "stateAccount" field for the document
                        document.reference.update("stateAccount", stateAccount)
                            .addOnSuccessListener {
                                // Update successful
                                // You can add any additional logic here
                            }
                            .addOnFailureListener { e ->
                                // Handle the error if the update fails
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the query failure
                }

            binding.state.text = "$state"
            getAllDriver()
        }
        binding.nameDriver.text = driverInfo.name
        Picasso.get().load(driverInfo.imageDriver).into(binding.imageDriver)
        binding.state.text = when(driverInfo.stateAccount){
            true -> "يعمل حاليا"
            false -> "محظور"
        }
        binding.etUserEmail.setText("${driverInfo.email}")
        binding.password.setText("${driverInfo.password}")

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    fun getAllDriver(){
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = requireContext().getSharedPreferences("Customer", Context.MODE_PRIVATE)
            var token = sharedPref.getString("Token","").toString()
            server.company.getAllDriver(token)
        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun anylisisDriver() {
        CoroutineScope(Dispatchers.IO).launch {
            var done = 0
            var reject = 0
            var pending = 0
            try {
                var books =  FirebaseFirestore.getInstance().collection("Book").whereEqualTo("idDriver",idDriver)
                    .get().await().toObjects(BookInfo_Data::class.java)
                // Calculate statistics based on book states
                for (book in books) {
                    when (book.stateBook) {
                        1 -> pending++
                        0 -> reject++
                        3 -> done++
                    }
                }
                binding.done.text = "${done}"
                binding.pending.text = "${pending}"
                binding.reject.text = "${reject}"

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}