package com.masar.masatapp1.Server

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.masar.masatapp1.Data.DriverInfo
import com.masar.masatapp1.server.cityCompany
import com.masar.masatapp1.server.namyCompany
import kotlinx.coroutines.tasks.await

class CompanyServer {

    private companion object {
        var listDriver = listOf<DriverInfo>()
    }

    fun signInCompany(email: String, password: String, onComplete: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        val companyUid = user.uid
                        FirebaseFirestore.getInstance().collection("Company")
                            .document(companyUid)
                            .get()
                            .addOnCompleteListener { companySnapshot ->
                                if (companySnapshot.isSuccessful) {
                                    onComplete(true)
                                } else {
                                    onComplete(false)
                                }
                            }
                    } else {
                        onComplete(false)
                    }
                } else {
                    onComplete(false)
                }
            }
    }

    fun signUpDelivery(context: Context,
        driverInfo: DriverInfo,
        onComplete: (Boolean) -> Unit
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(driverInfo.email, driverInfo.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var idDriver = Firebase.auth.uid.toString()
                    val sharedPref = context.getSharedPreferences("Customer", Context.MODE_PRIVATE)
                    var authSignUp = sharedPref.getString("Token","").toString()
                    if (authSignUp != null) {
                        val driver = driverInfo.copy(idDriver = idDriver)
                        FirebaseFirestore.getInstance()
                            .collection("Drivers")
                            .document(idDriver)
                            .set(driver)
                            .addOnSuccessListener {
                                onComplete(true) // User signup and data storage successful

                            }
                            .addOnFailureListener { e ->
                                onComplete(false) // User signup successful, but data storage failed
                                // Handle the error (e) here, if needed
                            }
                    }
                } else {
                    onComplete(false) // User signup failed
                    // Handle the signup failure here, if needed
                }
            }
    }

    // Function to check if an email is registered
    fun checkIfEmailIsRegistered(email: String, onComplete: (Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()

        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task: Task<SignInMethodQueryResult> ->
                if (task.isSuccessful) {
                    val result = task.result?.signInMethods
                    if (result != null && result.isNotEmpty()) {
                        // Email is registered
                        onComplete(true)
                    } else {
                        // Email is not registered
                        onComplete(false)
                    }
                } else {
                    // An error occurred while checking the email
                    // Handle the error appropriately
                    val exception = task.exception
                    onComplete(false) // Return false in case of an error
                    println("Error checking email: ${exception?.message}")
                }
            }
    }

    suspend fun getAllDriver(idCompany : String){
        try{
            getInfoCompany(idCompany)
            var REF_ListDriver = FirebaseFirestore.getInstance().collection("Drivers")
                .whereEqualTo("idCompany", idCompany)
            listDriver = REF_ListDriver.get().await().toObjects(DriverInfo::class.java)
            Log.e("ListDriver", listDriver.toString())

        }catch (e:Exception){
        }
    }
   fun getAllDriverList():List<DriverInfo>{
       return listDriver
   }
    fun getInfoCompany(idCompany : String){
        FirebaseFirestore.getInstance().collection("Company").document(idCompany).get()
            .addOnSuccessListener {
                 namyCompany = it.getString("name_Company").toString()
                 cityCompany = it.getString("cityCompany").toString()

            }
    }

}
