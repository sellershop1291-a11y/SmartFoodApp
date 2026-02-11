package com.example.smartfood.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfood.databinding.FragmentHomeBinding
import com.example.smartfood.utils.Resource
import com.example.smartfood.utils.gone
import com.example.smartfood.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupObservers()
        
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.search(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.loadHomeData()
                }
                return true
            }
        })
    }

    private fun setupRecyclerViews() {
        categoryAdapter = CategoryAdapter { category ->
            // Handle category click (filter foods)
            Toast.makeText(context, "${category.name} clicked", Toast.LENGTH_SHORT).show()
        }
        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        foodAdapter = FoodAdapter { food ->
            // Navigation to detail using safe args would be ideal, but for now using ID logic or simple navigation
            // val action = HomeFragmentDirections.actionHomeFragmentToFoodDetailFragment(food)
            // findNavController().navigate(action)
            Toast.makeText(context, "${food.name} clicked", Toast.LENGTH_SHORT).show()
            // Placeholder: findNavController().navigate(R.id.action_homeFragment_to_foodDetailFragment)
        }
        binding.rvPopular.apply {
            adapter = foodAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }
    }

    private fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> categoryAdapter.submitList(resource.data)
                is Resource.Error -> Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                is Resource.Loading -> {} // Optional: specific loading for categories
            }
        }

        viewModel.popularFoods.observe(viewLifecycleOwner) { resource ->
             when (resource) {
                is Resource.Success -> foodAdapter.submitList(resource.data)
                is Resource.Error -> Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                is Resource.Loading -> binding.progressBar.visible()
            }
            if (resource !is Resource.Loading) binding.progressBar.gone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
