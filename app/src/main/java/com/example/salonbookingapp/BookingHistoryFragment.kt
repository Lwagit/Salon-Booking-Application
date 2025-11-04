package com.example.salonbookingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    companion object {
        fun newInstance() = BookingHistoryFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_booking_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvBookings = view.findViewById(R.id.rvBookings)
        tvEmpty = view.findViewById(R.id.tvEmpty)

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
    }

    private fun openEditBooking(booking: Booking) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, EditBookingFragment.newInstance(booking) { updatedBooking ->
                val index = LocalBookings.bookings.indexOfFirst { it.id == updatedBooking.id }
                if (index != -1) {
                    LocalBookings.bookings[index] = updatedBooking
                    adapter.notifyItemChanged(index)
                }
            })
            .addToBackStack(null)
            .commit()
    }
}
