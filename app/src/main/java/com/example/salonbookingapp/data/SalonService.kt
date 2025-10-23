package com.example.salonbookingapp.data

import java.io.Serializable

data class SalonService(
    val name: String,
    val description: String,
    val price: Double,
    val duration: Int,
    val imageResId: Int = 0 // optional image reference, default = 0
) : Serializable
