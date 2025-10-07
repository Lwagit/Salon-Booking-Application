package com.example.salonbookingapp.data

import java.io.Serializable

data class SalonService(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val duration: Int = 0,
    val category: String = "",
    val imageResId: Int = 0
) : Serializable  // Add this line