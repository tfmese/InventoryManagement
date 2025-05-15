package com.tfdev.inventorymanagement.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfdev.inventorymanagement.data.Product
import com.tfdev.inventorymanagement.data.ProductDetails
import com.tfdev.inventorymanagement.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAllProducts()
                .catch { e ->
                    Timber.e(e, "Ürünler yüklenirken hata oluştu")
                    _uiState.value = UiState.Error("Ürünler yüklenirken bir hata oluştu")
                }
                .collect { products ->
                    _uiState.value = UiState.Success(products)
                }
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.searchProducts(query)
                .catch { e ->
                    Timber.e(e, "Ürün araması sırasında hata oluştu")
                    _uiState.value = UiState.Error("Ürün araması sırasında bir hata oluştu")
                }
                .collect { products ->
                    _uiState.value = UiState.Success(products)
                }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.insertProduct(product)
                loadProducts()
            } catch (e: Exception) {
                Timber.e(e, "Ürün eklenirken hata oluştu")
                _uiState.value = UiState.Error("Ürün eklenirken bir hata oluştu")
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.updateProduct(product)
                loadProducts()
            } catch (e: Exception) {
                Timber.e(e, "Ürün güncellenirken hata oluştu")
                _uiState.value = UiState.Error("Ürün güncellenirken bir hata oluştu")
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.deleteProduct(product)
                loadProducts()
            } catch (e: Exception) {
                Timber.e(e, "Ürün silinirken hata oluştu")
                _uiState.value = UiState.Error("Ürün silinirken bir hata oluştu")
            }
        }
    }

    fun getProductDetails(productId: Int): Flow<ProductDetails> {
        return combine(
            repository.getProductById(productId).filterNotNull(),
            repository.getWarehouseStocksForProduct(productId),
            repository.getTransactionsForProduct(productId)
        ) { product, warehouseStocks, transactions ->
            ProductDetails(
                product = product,
                warehouseStocks = warehouseStocks,
                transactions = transactions
            )
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val products: List<Product>) : UiState()
        data class Error(val message: String) : UiState()
    }
} 