package com.tfdev.inventorymanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfdev.inventorymanagement.data.AppDatabase
import com.tfdev.inventorymanagement.data.Product
import com.tfdev.inventorymanagement.adapter.ProductAdapter
import com.tfdev.inventorymanagement.data.ProductDao
import com.tfdev.inventorymanagement.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)
        productDao = db.productDao()

        adapter = ProductAdapter()
        binding.productRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.productRecyclerView.adapter = adapter

        binding.btnAdd.setOnClickListener {
            addProduct()
        }
        binding.btnProductToCustomer.setOnClickListener {
            val intent = Intent(this, CustomerActivity::class.java)
            startActivity(intent)
        }


        loadProducts()
    }

    private fun addProduct() {
        val name = binding.etName.text.toString()
        val desc = binding.etDescription.text.toString()
        val stock = binding.etStock.text.toString().toIntOrNull() ?: 0
        val price = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0
        val catId = binding.etCategoryId.text.toString().toIntOrNull() ?: 0
        val supId = binding.etSupplierId.text.toString().toIntOrNull() ?: 0

        val newProduct = Product(
            name = name,
            description = desc,
            stock = stock,
            price = price,
            categoryId = catId,
            supplierId = supId
        )

        lifecycleScope.launch {
            productDao.insert(newProduct)
            loadProducts()
        }
    }

    private fun loadProducts() {
        lifecycleScope.launch {
            val products = productDao.getAll()
            adapter.submitList(products)
        }
    }



}
