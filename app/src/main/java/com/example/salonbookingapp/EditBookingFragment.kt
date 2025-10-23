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
import com.example.salonbookingapp.data.FirestoreBookingHelper
import java.util.*

class EditBookingFragment : Fragment() {

    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var spinnerService: Spinner
    private lateinit var spinnerPayment: Spinner
    private lateinit var btnSaveChanges: Button

    private var bookingId: String = ""
    private var currentBooking: Booking? = null

    private val serviceList = listOf(
        "Men's Haircut",
        "Ladies Hair Wash & Style",
        "Beard Trim",
        "Hair Colour"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookingId = arguments?.getString(ARG_BOOKING_ID) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etDate = view.findViewById(R.id.etDate)
        etTime = view.findViewById(R.id.etTime)
        spinnerService = view.findViewById(R.id.spinnerService)
        spinnerPayment = view.findViewById(R.id.spinnerPayment)
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges)

        setupUI()
        loadBookingDetails()
    }

    private fun setupUI() {
        spinnerService.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, serviceList)
        spinnerPayment.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("Cash", "Card", "EFT"))

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                etDate.setText("$day/${month + 1}/$year")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        etTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                etTime.setText(String.format("%02d:%02d", hour, minute))
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        btnSaveChanges.setOnClickListener {
            if (currentBooking == null) return@setOnClickListener

            val updatedBooking = Booking(
                id = currentBooking!!.id,
                userId = currentBooking!!.userId,
                serviceName = spinnerService.selectedItem.toString(),
                servicePrice = getServicePrice(spinnerService.selectedItem.toString()),
                date = etDate.text.toString(),
                time = etTime.text.toString(),
                paymentMethod = spinnerPayment.selectedItem.toString()
            )

            FirestoreBookingHelper.updateBooking(updatedBooking) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Booking updated", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Failed to update booking", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadBookingDetails() {
        FirestoreBookingHelper.getUserBookings { list ->
            currentBooking = list.find { it.id == bookingId }
            currentBooking?.let { booking ->
                etDate.setText(booking.date)
                etTime.setText(booking.time)
                spinnerService.setSelection(serviceList.indexOf(booking.serviceName))
                spinnerPayment.setSelection(
                    (spinnerPayment.adapter as ArrayAdapter<String>).getPosition(booking.paymentMethod)
                )
            }
        }
    }

    private fun getServicePrice(serviceName: String): Double {
        return when (serviceName) {
            "Men's Haircut" -> 150.0
            "Ladies Hair Wash & Style" -> 200.0
            "Beard Trim" -> 100.0
            "Hair Colour" -> 350.0
            else -> 0.0
        }
    }

    companion object {
        private const val ARG_BOOKING_ID = "bookingId"

        fun newInstance(bookingId: String): EditBookingFragment {
            val fragment = EditBookingFragment()
            fragment.arguments = Bundle().apply { putString(ARG_BOOKING_ID, bookingId) }
            return fragment
        }
    }
}
