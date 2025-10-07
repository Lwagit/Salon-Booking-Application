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

class EditBookingFragment : Fragment() {

    private var bookingId: Int = 0
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var spinnerService: Spinner
    private lateinit var spinnerPayment: Spinner
    private lateinit var btnSave: Button
    private lateinit var dbHelper: BookingDatabaseHelper

    companion object {
        fun newInstance(bookingId: Int): EditBookingFragment {
            val fragment = EditBookingFragment()
            val args = Bundle()
            args.putInt("bookingId", bookingId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_edit_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = BookingDatabaseHelper(requireContext())
        bookingId = arguments?.getInt("bookingId") ?: 0

        etDate = view.findViewById(R.id.etDate)
        etTime = view.findViewById(R.id.etTime)
        spinnerService = view.findViewById(R.id.spinnerService)
        spinnerPayment = view.findViewById(R.id.spinnerPayment)
        btnSave = view.findViewById(R.id.btnSaveChanges)

        val services = listOf("Select Service", "Haircut", "Shave", "Massage", "Facial")
        spinnerService.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, services)

        val payments = listOf("Select Payment", "Cash", "Card", "EFT", "Mobile Payment")
        spinnerPayment.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, payments)

        val booking = dbHelper.getBookingById(bookingId)
        if (booking == null) {
            Toast.makeText(requireContext(), "Booking not found!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        etDate.setText(booking.date)
        etTime.setText(booking.time)
        spinnerService.setSelection(services.indexOf(booking.serviceName))
        spinnerPayment.setSelection(payments.indexOf(booking.paymentMethod))

        etDate.setOnClickListener { showDatePicker() }
        etTime.setOnClickListener { showTimePicker() }

        btnSave.setOnClickListener {
            val updatedBooking = Booking(
                id = bookingId,
                serviceName = spinnerService.selectedItem.toString(),
                servicePrice = booking.servicePrice,
                date = etDate.text.toString(),
                time = etTime.text.toString(),
                paymentMethod = spinnerPayment.selectedItem.toString()
            )

            if (updatedBooking.serviceName == "Select Service" || updatedBooking.paymentMethod == "Select Payment") {
                Toast.makeText(requireContext(), "Please select service and payment method", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dbHelper.updateBooking(updatedBooking)
            Toast.makeText(requireContext(), "Booking updated!", Toast.LENGTH_SHORT).show()

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
            { _, y, m, d -> etDate.setText("%02d/%02d/%d".format(d, m + 1, y)) },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, h, m -> etTime.setText(String.format("%02d:%02d", h, m)) },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}
