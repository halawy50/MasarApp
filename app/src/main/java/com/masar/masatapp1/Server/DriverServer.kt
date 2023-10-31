package com.masar.masatapp1.Server

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.masar.masatapp1.Data.BookInfo_Data
import com.masar.masatapp1.Data.DriverInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DriverServer {
    private companion object {
        var listBook = listOf<BookInfo_Data>()
    }
    fun getAllBook(idDriver : String){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                var REF_ListBook = FirebaseFirestore.getInstance().collection("Book")
                    .whereEqualTo("idDriver", idDriver)
                listBook = REF_ListBook.get().await().toObjects(BookInfo_Data::class.java)

            }catch (e:Exception){
            }
        }
    }
    fun getAllBookList():List<BookInfo_Data>{
        return listBook
    }

    fun acceptOrder(idBook: String, idDriver: String, seats: Int, complete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("Book")
        val driverCollection = db.collection("Drivers")

        val batch = db.batch()

        // Query the 'Book' collection for documents with matching 'idBook'
        val bookQuery = bookCollection.whereEqualTo("idBook", idBook)

        bookQuery.get()
            .addOnSuccessListener { querySnapshot ->
                // Loop through the matching documents
                for (document in querySnapshot) {
                    val bookDocRef = document.reference
                    // Update the 'stateBook' field to 2 for each matching document
                    batch.update(bookDocRef, "stateBook", 2)
                }

                // Query the 'Drivers' collection for documents with matching 'idDriver'
                val driverQuery = driverCollection.whereEqualTo("idDriver", idDriver)

                driverQuery.get()
                    .addOnSuccessListener { driverSnapshot ->
                        // Check if a driver document with the given 'idDriver' exists
                        if (!driverSnapshot.isEmpty) {
                            val driverDocRef = driverSnapshot.documents[0].reference
                            val driverInfo = driverSnapshot.documents[0].toObject(DriverInfo::class.java)

                            driverInfo?.let {
                                // Calculate the new number of 'approveSeats'
                                val newSeats = it.approveSeats - seats
                                // Update the 'approveSeats' field for the driver
                                batch.update(driverDocRef, "approveSeats", newSeats)
                            }

                            // Commit the batch update
                            batch.commit()
                                .addOnSuccessListener(OnSuccessListener<Void> {
                                    complete(true)
                                })
                                .addOnFailureListener(OnFailureListener {
                                    complete(false)
                                })
                        } else {
                            // Handle the case where no matching driver document was found
                            complete(false)
                        }
                    }
                    .addOnFailureListener(OnFailureListener {
                        complete(false)
                    })
            }
            .addOnFailureListener {
                // Handle any errors during the 'Book' collection query
                complete(false)
            }
    }
    fun doneOrder(idBook: String, idDriver: String, seats: Int, complete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("Book")
        val driverCollection = db.collection("Drivers")

        val batch = db.batch()

        // Query the 'Book' collection for documents with matching 'idBook'
        val bookQuery = bookCollection.whereEqualTo("idBook", idBook)

        bookQuery.get()
            .addOnSuccessListener { querySnapshot ->
                // Loop through the matching documents
                for (document in querySnapshot) {
                    val bookDocRef = document.reference
                    // Update the 'stateBook' field to 2 for each matching document
                    batch.update(bookDocRef, "stateBook", 3)
                }

                // Query the 'Drivers' collection for documents with matching 'idDriver'
                val driverQuery = driverCollection.whereEqualTo("idDriver", idDriver)

                driverQuery.get()
                    .addOnSuccessListener { driverSnapshot ->
                        // Check if a driver document with the given 'idDriver' exists
                        if (!driverSnapshot.isEmpty) {
                            val driverDocRef = driverSnapshot.documents[0].reference
                            val driverInfo = driverSnapshot.documents[0].toObject(DriverInfo::class.java)

                            driverInfo?.let {
                                // Calculate the new number of 'approveSeats'
                                val newSeats = it.approveSeats + seats
                                // Update the 'approveSeats' field for the driver
                                batch.update(driverDocRef, "approveSeats", newSeats)
                            }

                            // Commit the batch update
                            batch.commit()
                                .addOnSuccessListener(OnSuccessListener<Void> {
                                    complete(true)
                                })
                                .addOnFailureListener(OnFailureListener {
                                    complete(false)
                                })
                        } else {
                            // Handle the case where no matching driver document was found
                            complete(false)
                        }
                    }
                    .addOnFailureListener(OnFailureListener {
                        complete(false)
                    })
            }
            .addOnFailureListener {
                // Handle any errors during the 'Book' collection query
                complete(false)
            }
    }
    fun rejectOrder(idBook: String, idDriver: String, seats: Int, complete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val bookCollection = db.collection("Book")
        val driverCollection = db.collection("Drivers")

        val batch = db.batch()

        // Query the 'Book' collection for documents with matching 'idBook'
        val bookQuery = bookCollection.whereEqualTo("idBook", idBook)

        bookQuery.get()
            .addOnSuccessListener { querySnapshot ->
                // Loop through the matching documents
                for (document in querySnapshot) {
                    val bookDocRef = document.reference
                    // Update the 'stateBook' field to 2 for each matching document
                    batch.update(bookDocRef, "stateBook", 0)
                }

                // Query the 'Drivers' collection for documents with matching 'idDriver'
                val driverQuery = driverCollection.whereEqualTo("idDriver", idDriver)

            }
            .addOnFailureListener {
                // Handle any errors during the 'Book' collection query
                complete(false)
            }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getCounterSeats(idDriver: String, counter: (Number, Number) -> Unit) {
        // Access the Firestore instance and the "Drivers" collection
        val driversCollection = FirebaseFirestore.getInstance().collection("Drivers")

        // Create a query to find documents where "idDriver" equals the provided ID
        val query = driversCollection.whereEqualTo("idDriver", idDriver)

        // Execute the query
        query.get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Get the first document (assuming there's only one matching document)
                    val documentSnapshot = querySnapshot.documents[0]

                    // Retrieve the "numberOfSeats" and "approveSeats" fields from the document
                    val numberOfSeats = documentSnapshot.getLong("numberOfSeats")
                    val counterApprove = documentSnapshot.getLong("approveSeats")

                    // Log the number of seats (for debugging purposes)
                    Log.e("Seats", numberOfSeats.toString())

                    // Check if the retrieved values are not null
                    if (numberOfSeats != null && counterApprove != null) {
                        // Call the provided 'counter' lambda function with the retrieved values
                        counter(numberOfSeats, counterApprove)
                    } else {
                        // Handle the case where the data is missing or has unexpected types
                        // You might want to use default values or provide an error message
                        // Example: counter(-1, -1) or counterApprove(0, 0)
                    }
                } else {
                    // Handle the case where no matching documents were found
                    // You might want to log an error or take other actions
                }
            }
            .addOnFailureListener { e ->
                // Handle the failure to fetch the document
                // You might want to log the error or take other actions
            }
    }


}