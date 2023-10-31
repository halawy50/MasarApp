package com.masar.masatapp1.Server

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.masar.masatapp1.Data.BookInfo_Data
import com.masar.masatapp1.Data.DriverInfo
import com.masar.masatapp1.Data.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

class StudentServer {

    private companion object {
        var listBook = listOf<BookInfo_Data>()
        private var listDrivers = listOf<DriverInfo>()

    }

    fun signUpStudent(context: Context,name: String, email: String, password: String, onComplete: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val authSignUp = Firebase.auth.uid.toString()
                    if (authSignUp != null) {
                        val student = Student(name, "", "", true)
                        FirebaseFirestore.getInstance()
                            .collection("Student")
                            .document(authSignUp)
                            .set(student)
                            .addOnSuccessListener {
                                onComplete(true) // User signup and data storage successful

                                val sharedPref = context.getSharedPreferences("Customer", Context.MODE_PRIVATE)
                                val editor = sharedPref.edit()
                                editor.putString("Token",authSignUp)
                                editor.putInt("who",1)//Student
                                editor.apply()

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

    fun bookStudent( bookInfo : BookInfo_Data, onComplete: (Boolean) -> Unit) {
        Firebase.firestore.collection("Book")
            .add(bookInfo)
            .addOnSuccessListener {
                onComplete(true)
            }.addOnFailureListener {
                onComplete(false)
            }
        }

    suspend fun getAllBook_Activty(token_Student: String) {
        try {
            val listbookDEF =
                FirebaseFirestore.getInstance().collection("Book").whereEqualTo("idPerson", token_Student)
            val querySnapshot = listbookDEF.get().await()

            // Create a local mutable list to collect data
            val localList = mutableListOf<BookInfo_Data>()

            // Convert the query snapshot to a list of BookInfo_Data
            for (document in querySnapshot.documents) {
                val bookInfo = document.toObject(BookInfo_Data::class.java)
                if (bookInfo != null) {
                    localList.add(bookInfo)
                }
            }

            // Update listBook with the localList
            listBook = localList
        } catch (e: Exception) {
            // Handle exceptions here
        }
    }

   suspend fun getDataStudent(token_Student: String) : Student {
        try {
            var infoStudentDEF = FirebaseFirestore.getInstance().collection("Student").document(token_Student)
            var restult = infoStudentDEF.get().await().toObject(Student::class.java)
            Log.e("ListBook", token_Student+ restult.toString())
            return restult!!
        }catch (e:Exception){
          return  Student()
        }
    }
    fun updateDataStudent(name:String , age:String , shchool : String,token : String, onComplete: (Boolean) -> Unit) {
        val updatedData = mapOf(
            "nameStudent" to "${name}",
            "age" to "${age}",
            "schoolName" to "${shchool}",
        )
        Firebase.firestore.collection("Student").document(token).update(updatedData).addOnSuccessListener {
            onComplete(true)
        }.addOnFailureListener {
            onComplete(false)
        }
    }

    fun getAllBook():List<BookInfo_Data>{
        return listBook
    }


    fun getDriver(city: String, typeVechile: String, onComplete: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            withTimeoutOrNull(3000) {
                val querySnapshot = FirebaseFirestore.getInstance()
                    .collection("Company")
                    .whereEqualTo("state", true)
                    .whereEqualTo("cityCompany", city)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    // Assuming there's only one document matching the query
                    val idCompany = querySnapshot.documents[0].getString("idComapny")
                    if (idCompany != null) {
                        // Use idCompany here
                        listDrivers = FirebaseFirestore.getInstance()
                            .collection("Drivers")
                            .whereEqualTo("idCompany", idCompany)
                            .whereEqualTo("stateAccount", true)
                            .whereEqualTo("typeOfVechile", typeVechile)
                            .get()
                            .await()
                            .toObjects(DriverInfo::class.java).filter { it.approveSeats>0 }
                        if (listDrivers.isNotEmpty()){
                            onComplete(true)
                        }else{
                            onComplete(false)
                        }
                    } else {
                        // Handle the case where idCompany is null
                        onComplete(false)
                    }
                } else {
                    // Handle the case where no documents match the query
                    onComplete(false)
                }
            }
        }
    }


    fun getDriverList(): List<DriverInfo> {
        return listDrivers.toList()
    }

}