package com.example.smartfood.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfood.data.model.CartItem
import com.example.smartfood.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems
    val totalPrice: StateFlow<Double> = cartRepository.totalPrice

    fun updateQuantity(cartItem: CartItem, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(cartItem, quantity)
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItem)
        }
    }
}
