package com.example.smartfood.data.repository

import com.example.smartfood.data.model.User
import com.example.smartfood.utils.Constants
import com.example.smartfood.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    suspend fun login(email: String, pass: String): Resource<User>
    suspend fun signup(name: String, email: String, pass: String): Resource<User>
    fun logout()
    fun getCurrentUser(): User?
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, pass: String): Resource<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            val uid = result.user?.uid ?: return Resource.Error("User ID not found")
            
            val snapshot = firestore.collection(Constants.USERS_COLLECTION).document(uid).get().await()
            val user = snapshot.toObject(User::class.java)
            
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("User data not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun signup(name: String, email: String, pass: String): Resource<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val uid = result.user?.uid ?: return Resource.Error("Registration failed")
            
            val newUser = User(uid = uid, name = name, email = email)
            firestore.collection(Constants.USERS_COLLECTION).document(uid).set(newUser).await()
            
            Resource.Success(newUser)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUser(): User? {
        // This is a synchronous check, might need async fetch for full details
        // For now returning basic info if needed or just null
        return if(auth.currentUser != null) User(uid = auth.currentUser!!.uid, email = auth.currentUser!!.email ?: "") else null
    }
}
