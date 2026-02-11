package com.example.smartfood.data.model

import java.io.Serializable

data class Category(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = ""
)

data class Food(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val categoryId: String = "",
    val isPopular: Boolean = false
) : Serializable
