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
    private val bookings: List<Booking>,
    private val onEdit: (Booking) -> Unit,
    private val onDelete: (Booking) -> Unit
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount(): Int = bookings.size

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvServiceName: TextView = itemView.findViewById(R.id.tvServiceName)
        private val tvServicePrice: TextView = itemView.findViewById(R.id.tvServicePrice)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvPaymentMethod: TextView = itemView.findViewById(R.id.tvPaymentMethod)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(booking: Booking) {
            tvServiceName.text = booking.serviceName
            tvServicePrice.text = "R${booking.servicePrice.toInt()}"
            tvDate.text = booking.date
            tvTime.text = booking.time
            tvPaymentMethod.text = booking.paymentMethod

            btnEdit.setOnClickListener { onEdit(booking) }
            btnDelete.setOnClickListener { onDelete(booking) }
        }
    }
}
