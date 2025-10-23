package com.example.salonbookingapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salonbookingapp.R
import com.example.salonbookingapp.data.Booking
import com.example.salonbookingapp.data.FirestoreBookingHelper

class BookingHistoryFragment : Fragment() {

    private lateinit var rvBookings: RecyclerView
    private lateinit var tvEmpty: TextView
    private val bookingsList = mutableListOf<Booking>()
    private lateinit var adapter: BookingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_booking_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvBookings = view.findViewById(R.id.rvBookings)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        adapter = BookingAdapter(
            bookingsList,
            onEdit = { booking -> editBooking(booking.id) },
            onDelete = { booking -> deleteBooking(booking) }
        )

        rvBookings.layoutManager = LinearLayoutManager(requireContext())
        rvBookings.adapter = adapter

        loadBookings()
    }

    private fun loadBookings() {
        FirestoreBookingHelper.getUserBookings { list ->
            bookingsList.clear()
            bookingsList.addAll(list)
            adapter.notifyDataSetChanged()
            tvEmpty.visibility = if (bookingsList.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun editBooking(bookingId: String) {
        val fragment = EditBookingFragment.newInstance(bookingId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun deleteBooking(booking: Booking) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Booking")
            .setMessage("Are you sure you want to delete this booking?")
            .setPositiveButton("Yes") { _, _ ->
                FirestoreBookingHelper.deleteBooking(booking.id) { success ->
                    if (success) {
                        Toast.makeText(requireContext(), "Booking deleted", Toast.LENGTH_SHORT).show()
                        loadBookings()
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete booking", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
