package com.tfdev.inventorymanagement

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tfdev.inventorymanagement.adapter.CustomerAdapter
import com.tfdev.inventorymanagement.data.Customer
import com.tfdev.inventorymanagement.databinding.ActivityCustomerBinding
import com.tfdev.inventorymanagement.ui.customer.CustomerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerBinding
    private val viewModel: CustomerViewModel by viewModels()
    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = CustomerAdapter()
        binding.customerRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.customerRecyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnAddCustomer.setOnClickListener {
            addCustomer()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is CustomerViewModel.UiState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.customerRecyclerView.isVisible = false
                    }
                    is CustomerViewModel.UiState.Success -> {
                        binding.progressBar.isVisible = false
                        binding.customerRecyclerView.isVisible = true
                        adapter.submitList(state.customers)
                    }
                    is CustomerViewModel.UiState.Error -> {
                        binding.progressBar.isVisible = false
                        binding.customerRecyclerView.isVisible = true
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun addCustomer() {
        val name = binding.etCustomerName.text.toString()
        val address = binding.etCustomerAddress.text.toString()
        val phone = binding.etCustomerPhone.text.toString()
        val email = binding.etCustomerMail.text.toString()
        val city = binding.etCustomerCity.text.toString()

        val customer = Customer(
            name = name,
            address = address,
            phoneNumber = phone,
            email = email,
            city = city
        )

        viewModel.addCustomer(customer)
        clearInputs()
    }

    private fun clearInputs() {
        binding.etCustomerName.text.clear()
        binding.etCustomerAddress.text.clear()
        binding.etCustomerPhone.text.clear()
        binding.etCustomerMail.text.clear()
        binding.etCustomerCity.text.clear()
    }
}
