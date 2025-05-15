package com.tfdev.inventorymanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfdev.inventorymanagement.data.dao.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val customerDao: CustomerDao,
    private val orderDao: OrderDao,
    private val warehouseDao: WarehouseDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _dashboardStats = MutableStateFlow<DashboardStats?>(null)
    val dashboardStats: StateFlow<DashboardStats?> = _dashboardStats.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                val stats = combine(
                    productDao.getAllProducts(),
                    customerDao.getAllCustomers(),
                    orderDao.getAllOrders(),
                    warehouseDao.getAllWarehouses()
                ) { products, customers, orders, warehouses ->
                    DashboardStats(
                        totalProducts = products.size,
                        totalCustomers = customers.size,
                        totalOrders = orders.size,
                        totalWarehouses = warehouses.size
                    )
                }.first() // İlk değeri al

                _dashboardStats.value = stats
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                Timber.e(e, "Veri yükleme hatası")
                _uiState.value = UiState.Error("Veriler yüklenirken bir hata oluştu")
            }
        }
    }

    fun refreshData() {
        loadInitialData()
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    data class DashboardStats(
        val totalProducts: Int = 0,
        val totalCustomers: Int = 0,
        val totalOrders: Int = 0,
        val totalWarehouses: Int = 0
    )
} 