package com.example.salonbookingapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreBookingHelper {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private const val COLLECTION = "bookings"

    fun addBooking(booking: Booking, onComplete: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onComplete(false)

        val docRef = db.collection(COLLECTION).document()
        val id = docRef.id

        val data = mapOf(
            "id" to id,
            "userId" to user.uid,
            "serviceName" to booking.serviceName,
            "servicePrice" to booking.servicePrice,
            "date" to booking.date,
            "time" to booking.time,
            "paymentMethod" to booking.paymentMethod
        )

        docRef.set(data)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getUserBookings(onResult: (List<Booking>) -> Unit) {
        val user = auth.currentUser ?: return onResult(emptyList())

        db.collection(COLLECTION)
            .whereEqualTo("userId", user.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { doc ->
                    val id = doc.getString("id") ?: ""
                    val userId = doc.getString("userId") ?: ""
                    val serviceName = doc.getString("serviceName") ?: ""
                    val servicePrice = doc.getDouble("servicePrice") ?: 0.0
                    val date = doc.getString("date") ?: ""
                    val time = doc.getString("time") ?: ""
                    val paymentMethod = doc.getString("paymentMethod") ?: ""

                    Booking(
                        id = id,
                        userId = userId,
                        serviceName = serviceName,
                        servicePrice = servicePrice,
                        date = date,
                        time = time,
                        paymentMethod = paymentMethod
                    )
                }
                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun deleteBooking(bookingId: String, onComplete: (Boolean) -> Unit) {
        if (bookingId.isBlank()) return onComplete(false)
        db.collection(COLLECTION).document(bookingId)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun updateBooking(booking: Booking, onComplete: (Boolean) -> Unit) {
        if (booking.id.isBlank()) return onComplete(false)

        val data = mapOf(
            "serviceName" to booking.serviceName,
            "servicePrice" to booking.servicePrice,
            "date" to booking.date,
            "time" to booking.time,
            "paymentMethod" to booking.paymentMethod
        )

        db.collection(COLLECTION).document(booking.id)
            .update(data)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
