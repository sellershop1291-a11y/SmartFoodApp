package com.example.smartfood.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.smartfood.data.model.Food
import com.example.smartfood.databinding.FragmentFoodDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodDetailFragment : Fragment() {

    private var _binding: FragmentFoodDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private var food: Food? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Assuming "food" is passed as Serializable or Parcelable
            // For now using safe args pattern without generated classes manually or just Bundle
             @Suppress("DEPRECATION")
             food = it.getSerializable("food") as? Food
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
        setupListeners()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        
        food?.let {
            binding.tvFoodNameDetail.text = it.name
            binding.tvPriceDetail.text = "$${it.price}"
            binding.tvDescription.text = it.description
            binding.imgFoodDetail.load(it.imageUrl)
        }
    }

    private fun setupListeners() {
        binding.btnIncrease.setOnClickListener {
            viewModel.increaseQuantity()
        }

        binding.btnDecrease.setOnClickListener {
            viewModel.decreaseQuantity()
        }

        binding.btnAddToCart.setOnClickListener {
            food?.let { foodItem ->
                viewModel.addToCart(foodItem)
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun setupObservers() {
        viewModel.quantity.observe(viewLifecycleOwner) { qty ->
            binding.tvQuantity.text = qty.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
