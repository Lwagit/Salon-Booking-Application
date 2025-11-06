package com.example.salonbookingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salonbookingapp.MapFragment
import com.example.salonbookingapp.R
import com.example.salonbookingapp.ReviewsFragment
import com.example.salonbookingapp.ServiceAdapter
import com.example.salonbookingapp.data.SalonService

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter
    private val serviceList = mutableListOf<SalonService>()

    private lateinit var btnBookNow: Button
    private lateinit var btnViewMap: Button
    private lateinit var btnReviews: Button

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.rvServices)
        btnBookNow = view.findViewById(R.id.btnBookNow)
        btnViewMap = view.findViewById(R.id.btnViewMap)
        btnReviews = view.findViewById(R.id.btnReviews)

        setupRecyclerView()
        setupButtonListeners()

        return view
    }

    private fun setupRecyclerView() {
        // Load dummy services
        serviceList.apply {
            clear()
            add(SalonService("Men's Haircut", "Professional haircut with styling.", 150.0, 45, R.drawable.ic_haircut))
            add(SalonService("Ladies Hair Wash & Style", "Wash, blow dry, and style to perfection.", 200.0, 60, R.drawable.ic_facial))
            add(SalonService("Beard Trim", "Clean trim and edge styling for your beard.", 100.0, 30, R.drawable.ic_beard))
            add(SalonService("Hair Colour", "Full hair colouring using professional-grade products.", 350.0, 90, R.drawable.ic_color))
        }

        serviceAdapter = ServiceAdapter(serviceList) { /* optional click action */ }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = serviceAdapter
    }

    private fun setupButtonListeners() {
        // ✅ Book Now opens BookingFragment simply
        btnBookNow.setOnClickListener {
            val bookingFragment = BookingFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, bookingFragment)
                .addToBackStack(null)
                .commit()
        }

        // View Map
        btnViewMap.setOnClickListener {
            val mapFragment = MapFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, mapFragment)
                .addToBackStack(null)
                .commit()
        }

        // View Reviews
        btnReviews.setOnClickListener {
            val reviewsFragment = ReviewsFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, reviewsFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
