package com.example.salonbookingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.salonbookingapp.R
import com.example.salonbookingapp.data.SalonService

class BookingConfirmationFragment : Fragment() {

    private var selectedService: SalonService? = null

    companion object {
        fun newInstance(service: SalonService?): BookingConfirmationFragment {
            val fragment = BookingConfirmationFragment()
            val args = Bundle()
            args.putSerializable("service", service)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_booking_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        selectedService = arguments?.getSerializable("service") as? SalonService
        setupUI(view)
    }

    private fun setupUI(view: View) {
        val tvConfirmation = view.findViewById<TextView>(R.id.tvConfirmation)
        val tvBookingDetails = view.findViewById<TextView>(R.id.tvBookingDetails)
        val btnViewHistory = view.findViewById<Button>(R.id.btnViewHistory)
        val btnBackToHome = view.findViewById<Button>(R.id.btnBackToHome)

        selectedService?.let { service ->
            tvConfirmation.text = "✅ Booking Confirmed!"
            tvBookingDetails.text = """
                Service: ${service.name}
                Price: R${service.price.toInt()}
                Duration: ${service.duration} minutes
                Status: Confirmed
                Thank you for your booking!
                📞 Contact: 011 123 4567
                📍 Location: 123 Beauty Street, Johannesburg
            """.trimIndent()
        }

        btnViewHistory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BookingHistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        btnBackToHome.setOnClickListener { parentFragmentManager.popBackStack() }
    }
}
