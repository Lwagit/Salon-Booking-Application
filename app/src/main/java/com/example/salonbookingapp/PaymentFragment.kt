package com.example.salonbookingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.salonbookingapp.R
import com.example.salonbookingapp.data.SalonService

class PaymentFragment : Fragment() {

    private var selectedService: SalonService? = null

    companion object {
        fun newInstance(service: SalonService?): PaymentFragment {
            val fragment = PaymentFragment()
            val args = Bundle()
            args.putSerializable("service", service)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedService = arguments?.getSerializable("service") as? SalonService
        setupUI(view)
    }

    private fun setupUI(view: View) {
        val tvServiceInfo = view.findViewById<TextView>(R.id.tvServiceInfo)
        val tvTotalAmount = view.findViewById<TextView>(R.id.tvTotalAmount)
        val btnConfirmPayment = view.findViewById<Button>(R.id.btnConfirmPayment)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        selectedService?.let { service ->
            tvServiceInfo.text = "Service: ${service.name}"
            tvTotalAmount.text = "Total Amount: R ${service.price.toInt()}"
        }

        btnConfirmPayment.setOnClickListener {
            Toast.makeText(requireContext(), "Payment Successful ✅", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BookingHistoryFragment())
                .addToBackStack("history")
                .commit()
        }

        btnCancel.setOnClickListener { parentFragmentManager.popBackStack() }
    }
}
