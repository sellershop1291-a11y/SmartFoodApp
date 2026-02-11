package com.example.smartfood.data.repository

import com.example.smartfood.data.model.Category
import com.example.smartfood.data.model.Food
import com.example.smartfood.utils.Constants
import com.example.smartfood.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface FoodRepository {
    suspend fun getCategories(): Resource<List<Category>>
    suspend fun getPopularFoods(): Resource<List<Food>>
    suspend fun getFoodsByCategory(categoryId: String): Resource<List<Food>>
    suspend fun getAllFoods(): Resource<List<Food>>
    suspend fun searchFoods(query: String): Resource<List<Food>>
}

class FoodRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FoodRepository {

    override suspend fun getCategories(): Resource<List<Category>> {
        return try {
            val snapshot = firestore.collection(Constants.CATEGORIES_COLLECTION).get().await()
            val categories = snapshot.toObjects(Category::class.java)
            Resource.Success(categories)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error fetching categories")
        }
    }

    override suspend fun getPopularFoods(): Resource<List<Food>> {
        return try {
            val snapshot = firestore.collection(Constants.FOODS_COLLECTION)
                .whereEqualTo("popular", true)
                .get().await()
            val foods = snapshot.toObjects(Food::class.java)
            Resource.Success(foods)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error fetching popular foods")
        }
    }

    override suspend fun getFoodsByCategory(categoryId: String): Resource<List<Food>> {
        return try {
            val snapshot = firestore.collection(Constants.FOODS_COLLECTION)
                .whereEqualTo("categoryId", categoryId)
                .get().await()
            val foods = snapshot.toObjects(Food::class.java)
            Resource.Success(foods)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error fetching foods")
        }
    }

    override suspend fun getAllFoods(): Resource<List<Food>> {
        return try {
            val snapshot = firestore.collection(Constants.FOODS_COLLECTION).get().await()
            val foods = snapshot.toObjects(Food::class.java)
            Resource.Success(foods)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error fetching foods")
        }
    }
    
    override suspend fun searchFoods(query: String): Resource<List<Food>> {
        return try {
             // Basic search: fetching all and filtering locally as Firestory doesn't support full text search natively easily
             // Or we can use range filters for simple prefix match
             val snapshot = firestore.collection(Constants.FOODS_COLLECTION)
                 .whereGreaterThanOrEqualTo("name", query)
                 .whereLessThanOrEqualTo("name", query + "\uf8ff")
                 .get().await()
             val foods = snapshot.toObjects(Food::class.java)
             Resource.Success(foods)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Search failed")
        }
    }
}
