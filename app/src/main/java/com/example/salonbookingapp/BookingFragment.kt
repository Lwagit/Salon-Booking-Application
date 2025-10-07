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
import com.example.salonbookingapp.data.BookingDatabaseHelper
import java.util.*

class BookingFragment : Fragment() {

    private lateinit var spinnerService: Spinner
    private lateinit var spinnerPayment: Spinner
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSelectedTime: TextView
    private lateinit var btnSelectDate: Button
    private lateinit var btnSelectTime: Button
    private lateinit var btnBook: Button
    private lateinit var btnViewBookings: Button
    private lateinit var dbHelper: BookingDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = BookingDatabaseHelper(requireContext())

        spinnerService = view.findViewById(R.id.spinnerService)
        spinnerPayment = view.findViewById(R.id.spinnerPayment)
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate)
        tvSelectedTime = view.findViewById(R.id.tvSelectedTime)
        btnSelectDate = view.findViewById(R.id.btnSelectDate)
        btnSelectTime = view.findViewById(R.id.btnSelectTime)
        btnBook = view.findViewById(R.id.btnBook)
        btnViewBookings = view.findViewById(R.id.btnViewBookings)

        // Services
        val services = listOf("Select Service", "Haircut", "Shave", "Massage", "Facial")
        spinnerService.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, services)

        // Payments
        val payments = listOf("Select Payment", "Cash", "Card", "EFT", "Mobile Payment")
        spinnerPayment.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, payments)

        btnSelectDate.setOnClickListener { showDatePicker() }
        btnSelectTime.setOnClickListener { showTimePicker() }

        btnBook.setOnClickListener {
            val selectedService = spinnerService.selectedItem.toString()
            val selectedPayment = spinnerPayment.selectedItem.toString()
            val selectedDate = tvSelectedDate.text.toString()
            val selectedTime = tvSelectedTime.text.toString()

            if (selectedService == "Select Service" || selectedPayment == "Select Payment" ||
                selectedDate.isEmpty() || selectedTime.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Please complete all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val servicePrice = when (selectedService) {
                "Haircut" -> 250.0
                "Shave" -> 150.0
                "Massage" -> 400.0
                "Facial" -> 300.0
                else -> 0.0
            }

            val booking = Booking(
                serviceName = selectedService,
                servicePrice = servicePrice,
                date = selectedDate,
                time = selectedTime,
                paymentMethod = selectedPayment
            )

            dbHelper.addBooking(booking)
            Toast.makeText(requireContext(), "Booking saved!", Toast.LENGTH_SHORT).show()

            spinnerService.setSelection(0)
            spinnerPayment.setSelection(0)
            tvSelectedDate.text = ""
            tvSelectedTime.text = ""
        }

        btnViewBookings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BookingHistoryFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, y, m, d -> tvSelectedDate.text = "%02d/%02d/%d".format(d, m + 1, y) },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, h, m -> tvSelectedTime.text = String.format("%02d:%02d", h, m) },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    companion object {
        // This fixes your "Unresolved reference 'newInstance'" error
        fun newInstance(): BookingFragment {
            return BookingFragment()
        }
    }
}
