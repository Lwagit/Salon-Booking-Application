package com.example.salonbookingapp.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salonbookingapp.R
import com.example.salonbookingapp.data.Booking
import com.example.salonbookingapp.data.FirestoreBookingHelper
import com.example.salonbookingapp.data.LocalBookings
import com.example.salonbookingapp.data.SalonService
import java.util.*

class BookingFragment : Fragment() {

    private lateinit var spinnerService: Spinner
    private lateinit var tvServicePrice: TextView
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSelectedTime: TextView
    private lateinit var spinnerPayment: Spinner
    private lateinit var btnConfirmBooking: Button
    private lateinit var btnPickDate: Button
    private lateinit var btnPickTime: Button
    private lateinit var btnViewBookings: Button

    private val serviceList = listOf(
        SalonService("Men's Haircut", "Professional haircut", 150.0, 45, R.drawable.ic_haircut),
        SalonService("Ladies Hair Wash & Style", "Wash, blow dry & style", 200.0, 60, R.drawable.ic_haircut),
        SalonService("Beard Trim", "Clean trim and edge styling", 100.0, 30, R.drawable.ic_haircut),
        SalonService("Hair Colour", "Full hair colouring", 350.0, 90, R.drawable.ic_haircut)
    )

    private var selectedService: SalonService? = null
    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerService = view.findViewById(R.id.spinnerService)
        tvServicePrice = view.findViewById(R.id.tvServicePrice)
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate)
        tvSelectedTime = view.findViewById(R.id.tvSelectedTime)
        spinnerPayment = view.findViewById(R.id.spinnerPayment)
        btnConfirmBooking = view.findViewById(R.id.btnBook)
        btnPickDate = view.findViewById(R.id.btnSelectDate)
        btnPickTime = view.findViewById(R.id.btnSelectTime)
        btnViewBookings = view.findViewById(R.id.btnViewBookings)
        setupUI()
    }

    private fun setupUI() {
        spinnerService.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, serviceList.map { it.name })
        spinnerService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedService = serviceList[position]
                tvServicePrice.text = "Price: R ${selectedService?.price?.toInt()}"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedService = null
                tvServicePrice.text = ""
            }
        }

        spinnerPayment.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listOf("Cash", "Card", "EFT"))

        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                selectedDate = "$day/${month + 1}/$year"
                tvSelectedDate.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnPickTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                selectedTime = String.format("%02d:%02d", hour, minute)
                tvSelectedTime.text = selectedTime
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        btnConfirmBooking.setOnClickListener {
            if (selectedService == null || selectedDate.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(requireContext(), "Please complete all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val booking = Booking(
                serviceName = selectedService!!.name,
                servicePrice = selectedService!!.price,
                date = selectedDate,
                time = selectedTime,
                paymentMethod = spinnerPayment.selectedItem.toString()
            )

            // Add to local bookings instantly
            LocalBookings.bookings.add(booking)

            // Optional: still add to Firestore
            FirestoreBookingHelper.addBooking(booking) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "✅ Booking confirmed!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, BookingHistoryFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "❌ Failed to confirm booking", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnViewBookings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BookingHistoryFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}
