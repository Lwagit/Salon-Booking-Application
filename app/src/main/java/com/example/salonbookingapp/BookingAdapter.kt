package com.example.salonbookingapp.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salonbookingapp.R
import com.example.salonbookingapp.data.Booking

class BookingAdapter(
    private val bookings: MutableList<Booking>,
    private val listener: (Booking, String) -> Unit
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount(): Int = bookings.size

    inner class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvServiceName: TextView = view.findViewById(R.id.tvServiceName)
        private val tvServicePrice: TextView = view.findViewById(R.id.tvServicePrice)
        private val tvDate: TextView = view.findViewById(R.id.tvDate)
        private val tvTime: TextView = view.findViewById(R.id.tvTime)
        private val tvPaymentMethod: TextView = view.findViewById(R.id.tvPaymentMethod)
        private val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)

        fun bind(booking: Booking) {
            tvServiceName.text = booking.serviceName
            tvServicePrice.text = "R${booking.servicePrice.toInt()}"
            tvDate.text = booking.date
            tvTime.text = booking.time
            tvPaymentMethod.text = booking.paymentMethod

            btnEdit.setOnClickListener { listener(booking, "edit") }
            btnDelete.setOnClickListener { listener(booking, "delete") }
        }
    }
}
