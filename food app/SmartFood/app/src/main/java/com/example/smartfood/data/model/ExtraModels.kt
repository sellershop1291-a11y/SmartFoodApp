package com.example.smartfood.data.model

data class CartItem(
    val food: Food = Food(),
    var quantity: Int = 1
)

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: String = "Pending",
    val timestamp: Long = 0,
    val address: String = ""
)
