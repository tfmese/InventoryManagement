package com.tfdev.inventorymanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tfdev.inventorymanagement.adapter.CustomerAdapter
import com.tfdev.inventorymanagement.data.AppDatabase
import com.tfdev.inventorymanagement.data.Customer
import com.tfdev.inventorymanagement.data.CustomerDao
import com.tfdev.inventorymanagement.databinding.ActivityCustomerBinding
import kotlinx.coroutines.launch

class CustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerBinding
    private lateinit var db: AppDatabase
    private lateinit var customerDao: CustomerDao
    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)
        customerDao = db.customerDao()

        adapter = CustomerAdapter()
        binding.customerRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.customerRecyclerView.adapter = adapter

        binding.btnAddCustomer.setOnClickListener {
            addCustomer()
        }

        loadCustomers()
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

        lifecycleScope.launch {
            customerDao.insert(customer)
            loadCustomers()
            clearInputs()
        }
    }

    private fun loadCustomers() {
        lifecycleScope.launch {
            val customers = customerDao.getAll()
            adapter.submitList(customers)
        }
    }

    private fun clearInputs() {
        binding.etCustomerName.text.clear()
        binding.etCustomerAddress.text.clear()
        binding.etCustomerPhone.text.clear()
        binding.etCustomerMail.text.clear()
        binding.etCustomerCity.text.clear()
    }
}
