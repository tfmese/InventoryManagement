package com.tfdev.inventorymanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tfdev.inventorymanagement.data.AppDatabase
import com.tfdev.inventorymanagement.data.Product
import com.tfdev.inventorymanagement.data.ProductAdapter
import com.tfdev.inventorymanagement.data.ProductDao
import com.tfdev.inventorymanagement.ui.theme.InventoryManagementTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getDatabase(this)
        productDao = db.productDao()

        adapter = ProductAdapter()
        findViewById<RecyclerView>(R.id.productRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            addProduct()
        }

        loadProducts()
    }

    private fun addProduct() {
        val name = findViewById<EditText>(R.id.etName).text.toString()
        val desc = findViewById<EditText>(R.id.etDescription).text.toString()
        val stock = findViewById<EditText>(R.id.etStock).text.toString().toIntOrNull() ?: 0
        val price = findViewById<EditText>(R.id.etPrice).text.toString().toDoubleOrNull() ?: 0.0
        val catId = findViewById<EditText>(R.id.etCategoryId).text.toString().toIntOrNull() ?: 0
        val supId = findViewById<EditText>(R.id.etSupplierId).text.toString().toIntOrNull() ?: 0

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
