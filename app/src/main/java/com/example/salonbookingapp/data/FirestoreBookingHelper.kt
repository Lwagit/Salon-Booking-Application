package com.example.salonbookingapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object FirestoreBookingHelper {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private const val COLLECTION = "bookings"

    val bookingsList = mutableListOf<Booking>()
    private var listener: ListenerRegistration? = null

    fun startListening(onUpdate: (List<Booking>) -> Unit) {
        val user = auth.currentUser ?: return
        listener?.remove()
        listener = db.collection(COLLECTION)
            .whereEqualTo("userId", user.uid)
            .addSnapshotListener { snapshot, _ ->
                bookingsList.clear()
                snapshot?.documents?.forEach { doc ->
                    val booking = Booking(
                        id = doc.getString("id") ?: doc.id,
                        userId = doc.getString("userId") ?: "",
                        serviceName = doc.getString("serviceName") ?: "",
                        servicePrice = doc.getDouble("servicePrice") ?: 0.0,
                        date = doc.getString("date") ?: "",
                        time = doc.getString("time") ?: "",
                        paymentMethod = doc.getString("paymentMethod") ?: ""
                    )
                    bookingsList.add(booking)
                }
                onUpdate(bookingsList)
            }
    }

    fun stopListening() {
        listener?.remove()
        listener = null
    }

    fun addBooking(booking: Booking, onComplete: (Boolean) -> Unit) {
        val user = auth.currentUser ?: return onComplete(false)
        val docRef = db.collection(COLLECTION).document()
        booking.id = docRef.id
        booking.userId = user.uid

        val data = booking.toMap()
        docRef.set(data)
            .addOnSuccessListener {
                bookingsList.add(booking)
                onComplete(true)
            }
            .addOnFailureListener { onComplete(false) }
    }

    fun updateBooking(booking: Booking, onComplete: (Boolean) -> Unit) {
        if (booking.id.isBlank()) return onComplete(false)
        val docRef = db.collection(COLLECTION).document(booking.id)
        val data = booking.toMap()
        docRef.set(data)
            .addOnSuccessListener {
                val index = bookingsList.indexOfFirst { it.id == booking.id }
                if (index != -1) bookingsList[index] = booking
                onComplete(true)
            }
            .addOnFailureListener { onComplete(false) }
    }

    fun deleteBooking(booking: Booking, onComplete: (Boolean) -> Unit) {
        if (booking.id.isBlank()) return onComplete(false)
        db.collection(COLLECTION).document(booking.id)
            .delete()
            .addOnSuccessListener {
                bookingsList.removeAll { it.id == booking.id }
                onComplete(true)
            }
            .addOnFailureListener { onComplete(false) }
    }

    private fun Booking.toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "serviceName" to serviceName,
            "servicePrice" to servicePrice,
            "date" to date,
            "time" to time,
            "paymentMethod" to paymentMethod
        )
    }
}
