package com.example.salonbookingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salonbookingapp.R
import com.example.salonbookingapp.data.Booking
import com.example.salonbookingapp.data.BookingAdapter
import com.example.salonbookingapp.data.BookingDatabaseHelper

class BookingHistoryFragment : Fragment() {

    private lateinit var rvBookings: RecyclerView
    private lateinit var dbHelper: BookingDatabaseHelper
    private lateinit var bookings: MutableList<Booking>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_booking_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = BookingDatabaseHelper(requireContext())
        rvBookings = view.findViewById(R.id.rvBookings)

        loadBookings()
    }

    private fun loadBookings() {
        bookings = dbHelper.getAllBookings().toMutableList()
        rvBookings.layoutManager = LinearLayoutManager(requireContext())
        rvBookings.adapter = BookingAdapter(bookings) { booking, action ->
            when(action) {
                "edit" -> openEditBooking(booking)
                "delete" -> deleteBooking(booking)
            }
        }

        if (bookings.isEmpty()) {
            Toast.makeText(requireContext(), "No bookings found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openEditBooking(booking: Booking) {
        val fragment = EditBookingFragment.newInstance(booking.id)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun deleteBooking(booking: Booking) {
        val rowsDeleted = dbHelper.deleteBooking(booking.id)
        if (rowsDeleted > 0) {
            bookings.remove(booking)
            rvBookings.adapter?.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Booking deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadBookings() // refresh after returning
    }
}
