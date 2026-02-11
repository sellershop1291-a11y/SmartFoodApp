package com.example.smartfood.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.smartfood.R
import com.example.smartfood.databinding.FragmentCheckoutBinding
import com.example.smartfood.utils.Resource
import com.example.smartfood.utils.gone
import com.example.smartfood.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmOrder.setOnClickListener {
            val address = binding.etAddress.text.toString().trim()
            if (address.isNotEmpty()) {
                viewModel.placeOrder(address)
            } else {
                Toast.makeText(context, "Please enter address", Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalPrice.collectLatest {
                 binding.tvFinalTotal.text = String.format("Total: $%.2f", it)
            }
        }

        viewModel.orderState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_checkoutFragment_to_homeFragment)
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
