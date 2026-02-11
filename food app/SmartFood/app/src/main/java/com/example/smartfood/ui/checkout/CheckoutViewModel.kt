package com.example.smartfood.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfood.data.model.Order
import com.example.smartfood.data.repository.CartRepository
import com.example.smartfood.data.repository.OrderRepository
import com.example.smartfood.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _orderState = MutableLiveData<Resource<String>>()
    val orderState: LiveData<Resource<String>> = _orderState

    val totalPrice = cartRepository.totalPrice

    fun placeOrder(address: String) {
        _orderState.value = Resource.Loading()
        viewModelScope.launch {
            val cartItems = cartRepository.cartItems.first()
            val total = cartRepository.totalPrice.first()
            val userId = auth.currentUser?.uid ?: ""

            if (userId.isEmpty()) {
                _orderState.value = Resource.Error("User not logged in")
                return@launch
            }

            val order = Order(
                userId = userId,
                items = cartItems,
                totalPrice = total,
                status = "Placed",
                timestamp = System.currentTimeMillis(),
                address = address
            )

            val result = orderRepository.placeOrder(order)
            if (result is Resource.Success) {
                cartRepository.clearCart()
            }
            _orderState.value = result
        }
    }
}
