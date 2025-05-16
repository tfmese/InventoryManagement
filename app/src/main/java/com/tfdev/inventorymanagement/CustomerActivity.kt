package com.tfdev.inventorymanagement

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tfdev.inventorymanagement.adapter.CustomerAdapter
import com.tfdev.inventorymanagement.data.entity.Customer
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Müşteriler"
        }

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        @Suppress("DEPRECATION")
        super.onBackPressed()
    }

    private fun setupRecyclerView() {
        adapter = CustomerAdapter(
            onItemClick = { _ ->
                // TODO: Müşteri detaylarına git
            },
            onEditClick = { _ ->
                // TODO: Müşteri düzenleme
            },
            onDeleteClick = { _ ->
                // TODO: Müşteri silme
            }
        )

        binding.customerRecyclerView.apply {
            adapter = this@CustomerActivity.adapter
            layoutManager = LinearLayoutManager(this@CustomerActivity)
        }
    }

    private fun setupClickListeners() {
        binding.btnAddCustomer.setOnClickListener {
            addCustomer()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.allCustomers.observe(this@CustomerActivity) { customers ->
                adapter.submitList(customers)
            }
        }
    }

    private fun addCustomer() {
        val name = binding.etCustomerName.text?.toString().orEmpty()
        val address = binding.etCustomerAddress.text?.toString().orEmpty()
        val phone = binding.etCustomerPhone.text?.toString().orEmpty()
        val email = binding.etCustomerMail.text?.toString().orEmpty()
        val city = binding.etCustomerCity.text?.toString().orEmpty()

        if (validateInputs()) {
            val customer = Customer(
                name = name,
                address = address,
                phoneNumber = phone,
                email = email,
                city = city
            )

            viewModel.saveCustomer(customer)
            clearInputs()
            Snackbar.make(binding.root, "Müşteri başarıyla eklendi", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        val inputs = listOf(
            binding.etCustomerName to "Müşteri adı boş olamaz",
            binding.etCustomerAddress to "Adres boş olamaz",
            binding.etCustomerPhone to "Telefon numarası boş olamaz",
            binding.etCustomerMail to "E-posta boş olamaz",
            binding.etCustomerCity to "Şehir boş olamaz"
        )

        inputs.forEach { (input, error) ->
            if (input.text?.toString().isNullOrBlank()) {
                input.error = error
                isValid = false
            }
        }
        return isValid
    }

    private fun clearInputs() {
        binding.etCustomerName.text?.clear()
        binding.etCustomerAddress.text?.clear()
        binding.etCustomerPhone.text?.clear()
        binding.etCustomerMail.text?.clear()
        binding.etCustomerCity.text?.clear()
    }
}
