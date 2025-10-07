package com.example.salonbookingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salonbookingapp.data.SalonService

class ServiceAdapter(
    private val services: List<SalonService>,
    private val onItemClick: (SalonService) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvServiceName: TextView = itemView.findViewById(R.id.tvServiceName)
        private val tvServiceDescription: TextView = itemView.findViewById(R.id.tvServiceDescription)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        private val ivService: ImageView = itemView.findViewById(R.id.ivService)

        fun bind(service: SalonService, onItemClick: (SalonService) -> Unit) {
            tvServiceName.text = service.name
            tvServiceDescription.text = service.description
            tvPrice.text = "R ${service.price.toInt()}"
            tvDuration.text = "${service.duration} min"

            ivService.setImageResource(
                if (service.imageResId != 0) service.imageResId
                else android.R.drawable.ic_menu_gallery
            )

            itemView.setOnClickListener { onItemClick(service) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position], onItemClick)
    }

    override fun getItemCount(): Int = services.size
}
