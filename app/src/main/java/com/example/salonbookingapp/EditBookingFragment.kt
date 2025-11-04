package com.example.salonbookingapp.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.salonbookingapp.R
import com.example.salonbookingapp.data.Booking
import com.example.salonbookingapp.data.LocalBookings
import java.util.*

class EditBookingFragment : Fragment() {

    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var spinnerService: Spinner
    private lateinit var spinnerPayment: Spinner
    private lateinit var btnSaveChanges: Button

    private var booking: Booking? = null
    private val serviceList = listOf("Men's Haircut", "Ladies Hair Wash & Style", "Beard Trim", "Hair Colour")
    private val paymentList = listOf("Cash", "Card", "EFT")

    companion object {
        private const val ARG_BOOKING = "booking"
        fun newInstance(booking: Booking, onUpdated: (Booking) -> Unit) = EditBookingFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_BOOKING, booking) }
            this.onUpdated = onUpdated
        }
    }

    private var onUpdated: ((Booking) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_edit_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etDate = view.findViewById(R.id.etDate)
        etTime = view.findViewById(R.id.etTime)
        spinnerService = view.findViewById(R.id.spinnerService)
        spinnerPayment = view.findViewById(R.id.spinnerPayment)
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges)

        booking = arguments?.getSerializable(ARG_BOOKING) as? Booking
        if (booking == null) { parentFragmentManager.popBackStack(); return }

        spinnerService.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, serviceList)
        spinnerPayment.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, paymentList)

        etDate.setText(booking!!.date)
        etTime.setText(booking!!.time)
        spinnerService.setSelection(serviceList.indexOf(booking!!.serviceName))
        spinnerPayment.setSelection(paymentList.indexOf(booking!!.paymentMethod))

        etDate.setOnClickListener { pickDate() }
        etTime.setOnClickListener { pickTime() }

        btnSaveChanges.setOnClickListener {
            booking!!.date = etDate.text.toString()
            booking!!.time = etTime.text.toString()
            booking!!.serviceName = spinnerService.selectedItem.toString()
            booking!!.paymentMethod = spinnerPayment.selectedItem.toString()

            // Update locally
            val index = LocalBookings.bookings.indexOfFirst { it.id == booking!!.id }
            if (index != -1) {
                LocalBookings.bookings[index] = booking!!
            }

            onUpdated?.invoke(booking!!)
            Toast.makeText(requireContext(), "Booking updated ✅", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun pickDate() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            etDate.setText("$day/${month + 1}/$year")
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun pickTime() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(requireContext(), { _, hour, minute ->
            etTime.setText(String.format("%02d:%02d", hour, minute))
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }
}
