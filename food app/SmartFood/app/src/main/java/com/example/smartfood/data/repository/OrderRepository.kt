package com.example.smartfood.data.repository

import com.example.smartfood.data.model.Order
import com.example.smartfood.utils.Constants
import com.example.smartfood.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface OrderRepository {
    suspend fun placeOrder(order: Order): Resource<String>
}

class OrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRepository {

    override suspend fun placeOrder(order: Order): Resource<String> {
        return try {
            val docRef = firestore.collection(Constants.ORDERS_COLLECTION).document()
            val orderWithId = order.copy(id = docRef.id)
            docRef.set(orderWithId).await()
            Resource.Success(docRef.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to place order")
        }
    }
}
