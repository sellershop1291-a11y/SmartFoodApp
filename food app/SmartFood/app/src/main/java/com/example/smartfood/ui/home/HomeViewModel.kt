package com.example.smartfood.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfood.data.model.Category
import com.example.smartfood.data.model.Food
import com.example.smartfood.data.repository.FoodRepository
import com.example.smartfood.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _categories = MutableLiveData<Resource<List<Category>>>()
    val categories: LiveData<Resource<List<Category>>> = _categories

    private val _popularFoods = MutableLiveData<Resource<List<Food>>>()
    val popularFoods: LiveData<Resource<List<Food>>> = _popularFoods

    private val _allFoods = MutableLiveData<Resource<List<Food>>>()
    val allFoods: LiveData<Resource<List<Food>>> = _allFoods

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        fetchCategories()
        fetchPopularFoods()
        fetchAllFoods()
    }

    private fun fetchCategories() {
        _categories.value = Resource.Loading()
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }

    private fun fetchPopularFoods() {
        _popularFoods.value = Resource.Loading()
        viewModelScope.launch {
            _popularFoods.value = repository.getPopularFoods()
        }
    }

    private fun fetchAllFoods() {
        _allFoods.value = Resource.Loading()
        viewModelScope.launch {
            _allFoods.value = repository.getAllFoods()
        }
    }
    
    fun search(query: String) {
        _allFoods.value = Resource.Loading()
        viewModelScope.launch {
            _allFoods.value = repository.searchFoods(query)
        }
    }
}
