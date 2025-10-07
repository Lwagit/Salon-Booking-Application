package com.example.salonbookingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salonbookingapp.data.SalonService
import com.example.salonbookingapp.utils.PrefManager
import com.example.salonbookingapp.fragments.BookingFragment

class HomeFragment : Fragment() {

    private lateinit var prefManager: PrefManager
    private lateinit var rvServices: RecyclerView
    private lateinit var tvWelcome: TextView
    private lateinit var btnBookNow: Button
    private lateinit var btnViewMap: Button
    private lateinit var btnReviews: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())
        tvWelcome = view.findViewById(R.id.tvWelcome)
        rvServices = view.findViewById(R.id.rvServices)
        btnBookNow = view.findViewById(R.id.btnBookNow)
        btnViewMap = view.findViewById(R.id.btnViewMap)
        btnReviews = view.findViewById(R.id.btnReviews) // Connect Reviews button

        setupUI()
        loadServices()
    }

    private fun setupUI() {
        val user = prefManager.getUser()
        tvWelcome.text = "Welcome, ${user?.name ?: "Guest"}!"

        btnBookNow.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BookingFragment.newInstance())
                .addToBackStack("booking")
                .commit()
        }

        btnViewMap.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MapFragment())
                .addToBackStack("map")
                .commit()
        }

        btnReviews.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ReviewsFragment()) // Works now
                .addToBackStack("reviews")
                .commit()
        }
    }

    private fun loadServices() {
        val services = listOf(
            SalonService("1", "Haircut & Styling", "Professional haircut and styling", 250.0, 60, "Hair", R.drawable.ic_haircut),
            SalonService("2", "Hair Coloring", "Full hair coloring treatment", 450.0, 120, "Hair", R.drawable.ic_color),
            SalonService("3", "Manicure", "Luxury manicure with polish", 180.0, 45, "Nails", R.drawable.ic_manicure),
            SalonService("4", "Pedicure", "Relaxing foot care treatment", 220.0, 60, "Nails", R.drawable.ic_beard),
            SalonService("5", "Facial Treatment", "Revitalizing facial care", 350.0, 90, "Skin", R.drawable.ic_facial),
            SalonService("6", "Full Body Massage", "Relaxing full body massage", 400.0, 60, "Spa", R.drawable.ic_massage),
            SalonService("7", "Bridal Makeup", "Professional bridal makeup", 600.0, 120, "Makeup", R.drawable.ic_makeup),
            SalonService("8", "Beard Trim & Style", "Professional beard grooming", 150.0, 30, "Grooming", R.drawable.ic_beard)
        )

        val adapter = ServiceAdapter(services) { _ ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BookingFragment.newInstance())
                .addToBackStack("booking")
                .commit()
        }

        rvServices.adapter = adapter
        rvServices.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}
