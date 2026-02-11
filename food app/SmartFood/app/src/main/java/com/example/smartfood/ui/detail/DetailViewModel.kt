package com.example.smartfood.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfood.data.model.Food
import com.example.smartfood.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _quantity = MutableLiveData(1)
    val quantity: LiveData<Int> = _quantity

    fun increaseQuantity() {
        _quantity.value = (_quantity.value ?: 1) + 1
    }

    fun decreaseQuantity() {
        val current = _quantity.value ?: 1
        if (current > 1) {
            _quantity.value = current - 1
        }
    }

    fun addToCart(food: Food) {
        viewModelScope.launch {
            cartRepository.addToCart(food, _quantity.value ?: 1)
        }
    }
}
