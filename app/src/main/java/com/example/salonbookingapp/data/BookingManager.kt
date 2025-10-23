package com.example.salonbookingapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

object BookingManager {

    private val firestore = FirebaseFirestore.getInstance()
    private val bookingsCollection = firestore.collection("bookings")
    private var listener: ListenerRegistration? = null

    val bookings = mutableListOf<Booking>()

    fun startListening(onUpdate: (List<Booking>) -> Unit) {
        listener = bookingsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            bookings.clear()
            snapshot?.documents?.forEach { doc ->
                val booking = doc.toObject(Booking::class.java)
                booking?.let { bookings.add(it.copy(id = doc.id)) }
            }
            onUpdate(bookings)
        }
    }

    fun stopListening() {
        listener?.remove()
        listener = null
    }

    fun addBooking(booking: Booking, onComplete: (Boolean) -> Unit) {
        bookingsCollection.add(booking)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun updateBooking(booking: Booking, onComplete: (Boolean) -> Unit) {
        bookingsCollection.document(booking.id).set(booking)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun deleteBooking(booking: Booking, onComplete: (Boolean) -> Unit) {
        bookingsCollection.document(booking.id).delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
