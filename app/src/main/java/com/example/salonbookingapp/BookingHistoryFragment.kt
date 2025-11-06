package com.example.salonbookingapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salonbookingapp.R
import com.example.salonbookingapp.data.Booking
import com.example.salonbookingapp.data.LocalBookings

class BookingHistoryFragment : Fragment() {

    private lateinit var rvBookings: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: BookingAdapter
    private lateinit var btnShareBooking: Button

    companion object {
        fun newInstance() = BookingHistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_booking_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvBookings = view.findViewById(R.id.rvBookings)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        btnShareBooking = view.findViewById(R.id.btnShareBooking)

        adapter = BookingAdapter(LocalBookings.bookings) { booking, action ->
            when (action) {
                "edit" -> openEditBooking(booking)
                "delete" -> {
                    LocalBookings.bookings.remove(booking)
                    adapter.notifyDataSetChanged()
                    tvEmpty.visibility = if (LocalBookings.bookings.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        rvBookings.layoutManager = LinearLayoutManager(requireContext())
        rvBookings.adapter = adapter

        tvEmpty.visibility = if (LocalBookings.bookings.isEmpty()) View.VISIBLE else View.GONE

        btnShareBooking.setOnClickListener {
            if (LocalBookings.bookings.isNotEmpty()) {
                val latestBooking = LocalBookings.bookings.last()
                shareBookingByEmail(latestBooking)
            } else {
                Toast.makeText(requireContext(), "No bookings to share.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openEditBooking(booking: Booking) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                EditBookingFragment.newInstance(booking) { updatedBooking ->
                    val index = LocalBookings.bookings.indexOfFirst { it.id == updatedBooking.id }
                    if (index != -1) {
                        LocalBookings.bookings[index] = updatedBooking
                        adapter.notifyItemChanged(index)
                    }
                }
            )
            .addToBackStack(null)
            .commit()
    }

    private fun shareBookingByEmail(booking: Booking) {
        val subject = "My Salon Booking Details"
        val message = """
            Hi there,

            Here are my salon booking details:

            • Service: ${booking.serviceName}
            • Date: ${booking.date}
            • Time: ${booking.time}
            • Price: R${booking.servicePrice}

            Sent from Salon Booking App 💇‍♀️
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            startActivity(Intent.createChooser(intent, "Send booking via email..."))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "No email app found on this device.", Toast.LENGTH_SHORT).show()
        }
    }
}
