package com.tfdev.inventorymanagement

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.tfdev.inventorymanagement.databinding.ActivityMainBinding
import com.tfdev.inventorymanagement.ui.product.ProductActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupSwipeRefresh()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.cardProducts.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }

        binding.cardCustomers.setOnClickListener {
            startActivity(Intent(this, CustomerActivity::class.java))
        }

        binding.cardOrders.setOnClickListener {
            // TODO: Implement OrderActivity
            showNotImplementedMessage()
        }

        binding.cardWarehouses.setOnClickListener {
            // TODO: Implement WarehouseActivity
            showNotImplementedMessage()
        }

        binding.cardCategories.setOnClickListener {
            // TODO: Implement CategoryActivity
            showNotImplementedMessage()
        }

        binding.cardSuppliers.setOnClickListener {
            // TODO: Implement SupplierActivity
            showNotImplementedMessage()
        }

        binding.cardShipments.setOnClickListener {
            // TODO: Implement ShipmentActivity
            showNotImplementedMessage()
        }

        binding.cardInventory.setOnClickListener {
            // TODO: Implement InventoryActivity
            showNotImplementedMessage()
        }
    }

    private fun showNotImplementedMessage() {
        Snackbar.make(binding.root, "Bu özellik henüz uygulanmadı", Snackbar.LENGTH_SHORT).show()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
    }

    private fun observeViewModel() {
        // UI State'i gözlemle
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is MainViewModel.UiState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.contentLayout.isVisible = false
                    }
                    is MainViewModel.UiState.Success -> {
                        binding.progressBar.isVisible = false
                        binding.contentLayout.isVisible = true
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is MainViewModel.UiState.Error -> {
                        binding.progressBar.isVisible = false
                        binding.contentLayout.isVisible = true
                        binding.swipeRefresh.isRefreshing = false
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        // Dashboard istatistiklerini gözlemle
        lifecycleScope.launch {
            viewModel.dashboardStats.collectLatest { stats ->
                stats?.let {
                    binding.tvProductCount.text = it.totalProducts.toString()
                    binding.tvCustomerCount.text = it.totalCustomers.toString()
                    binding.tvOrderCount.text = it.totalOrders.toString()
                    binding.tvWarehouseCount.text = it.totalWarehouses.toString()
                }
            }
        }
    }
}
