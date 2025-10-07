package com.example.salonbookingapp.data

import java.io.Serializable

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String
) : Serializable
