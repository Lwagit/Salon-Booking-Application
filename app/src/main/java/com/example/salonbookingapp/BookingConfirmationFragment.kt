package com.example.salonbookingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.salonbookingapp.R

class BookingConfirmationFragment : Fragment() {

    private lateinit var tvBookingDetails: TextView
    private lateinit var btnBackHome: Button
    private lateinit var btnViewHistory: Button

    companion object {
        fun newInstance(details: String) = BookingConfirmationFragment().apply {
            arguments = Bundle().apply { putString("details", details) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_booking_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvBookingDetails = view.findViewById(R.id.tvBookingDetails)
        btnBackHome = view.findViewById(R.id.btnBackToHome)
        btnViewHistory = view.findViewById(R.id.btnViewHistory)

        tvBookingDetails.text = arguments?.getString("details") ?: "Booking details"

        btnBackHome.setOnClickListener { parentFragmentManager.popBackStack() }
        btnViewHistory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BookingHistoryFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}
