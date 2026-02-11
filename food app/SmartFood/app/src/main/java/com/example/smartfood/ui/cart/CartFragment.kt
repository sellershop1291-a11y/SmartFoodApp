package com.example.smartfood.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartfood.R
import com.example.smartfood.databinding.FragmentCartBinding
import com.example.smartfood.utils.gone
import com.example.smartfood.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        binding.btnCheckout.setOnClickListener {
            if (viewModel.cartItems.value.isNotEmpty()) {
                findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onPlusClick = { item -> viewModel.updateQuantity(item, item.quantity + 1) },
            onMinusClick = { item -> viewModel.updateQuantity(item, item.quantity - 1) },
            onDeleteClick = { item -> viewModel.removeFromCart(item) }
        )
        binding.rvCart.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItems.collectLatest { items ->
                cartAdapter.submitList(items)
                if (items.isEmpty()) {
                    binding.tvEmptyCart.visible()
                    binding.rvCart.gone()
                } else {
                    binding.tvEmptyCart.gone()
                    binding.rvCart.visible()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalPrice.collectLatest { total ->
                binding.tvTotalPrice.text = String.format("$%.2f", total)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
