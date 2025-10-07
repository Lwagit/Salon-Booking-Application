package com.example.salonbookingapp.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salonbookingapp.R

class BookingAdapter(
    private val bookings: List<Booking>,
    private val listener: (Booking, String) -> Unit
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun getItemCount(): Int = bookings.size

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.tvService.text = booking.serviceName
        holder.tvServicePrice.text = "R${booking.servicePrice}"
        holder.tvDate.text = booking.date
        holder.tvTime.text = booking.time
        holder.tvPayment.text = booking.paymentMethod

        holder.btnEdit.setOnClickListener { listener(booking, "edit") }
        holder.btnDelete.setOnClickListener { listener(booking, "delete") }
    }

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvService: TextView = itemView.findViewById(R.id.tvServiceName)
        val tvServicePrice: TextView = itemView.findViewById(R.id.tvServicePrice)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvPayment: TextView = itemView.findViewById(R.id.tvPaymentMethod)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }
}
