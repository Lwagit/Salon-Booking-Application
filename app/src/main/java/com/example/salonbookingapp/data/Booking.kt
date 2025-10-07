package com.example.salonbookingapp.data

data class Booking(
    val id: Int = 0,                 // for database
    val serviceName: String,         // corresponds to spinner selection
    val servicePrice: Double = 0.0, // optional if you want to store price
    val date: String,
    val time: String,
    val paymentMethod: String
)
