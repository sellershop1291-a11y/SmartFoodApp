package com.example.smartfood.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfood.data.model.User
import com.example.smartfood.data.repository.AuthRepository
import com.example.smartfood.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<User>>()
    val loginState: LiveData<Resource<User>> = _loginState

    private val _signupState = MutableLiveData<Resource<User>>()
    val signupState: LiveData<Resource<User>> = _signupState

    fun login(email: String, pass: String) {
        _loginState.value = Resource.Loading()
        viewModelScope.launch {
            _loginState.value = repository.login(email, pass)
        }
    }

    fun signup(name: String, email: String, pass: String) {
        _signupState.value = Resource.Loading()
        viewModelScope.launch {
            _signupState.value = repository.signup(name, email, pass)
        }
    }
}
