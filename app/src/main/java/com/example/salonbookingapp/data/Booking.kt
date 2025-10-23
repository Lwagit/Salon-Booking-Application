package com.example.salonbookingapp.data

data class Booking(
    var id: String = "",
    var userId: String = "",
    var serviceName: String = "",
    var servicePrice: Double = 0.0,
    var date: String = "",
    var time: String = "",
    var paymentMethod: String = ""
)
