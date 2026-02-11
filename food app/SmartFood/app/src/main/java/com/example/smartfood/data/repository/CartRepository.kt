package com.example.smartfood.data.repository

import com.example.smartfood.data.model.CartItem
import com.example.smartfood.data.model.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface CartRepository {
    val cartItems: StateFlow<List<CartItem>>
    val totalPrice: StateFlow<Double>
    
    suspend fun addToCart(food: Food, quantity: Int)
    suspend fun removeFromCart(cartItem: CartItem)
    suspend fun updateQuantity(cartItem: CartItem, quantity: Int)
    suspend fun clearCart()
}

@Singleton
class CartRepositoryImpl @Inject constructor() : CartRepository {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    override val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    override val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    override suspend fun addToCart(food: Food, quantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val existingItem = currentList.find { it.food.id == food.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentList.add(CartItem(food, quantity))
        }
        
        _cartItems.value = currentList
        calculateTotal()
    }

    override suspend fun removeFromCart(cartItem: CartItem) {
        val currentList = _cartItems.value.toMutableList()
        currentList.remove(cartItem)
        _cartItems.value = currentList
        calculateTotal()
    }

    override suspend fun updateQuantity(cartItem: CartItem, quantity: Int) {
        val currentList = _cartItems.value.toMutableList()
         val index = currentList.indexOfFirst { it.food.id == cartItem.food.id }
        if (index != -1) {
            if (quantity > 0) {
                // Create a new instance to trigger flow update if needed, or just mutable
                // data classes are immutable so better replace
                currentList[index] = currentList[index].copy(quantity = quantity)
            } else {
                currentList.removeAt(index)
            }
            _cartItems.value = currentList
            calculateTotal()
        }
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
        calculateTotal()
    }

    private fun calculateTotal() {
        _totalPrice.value = _cartItems.value.sumOf { it.food.price * it.quantity }
    }
}
