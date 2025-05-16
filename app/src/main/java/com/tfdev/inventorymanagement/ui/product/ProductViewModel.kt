package com.tfdev.inventorymanagement.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfdev.inventorymanagement.data.dao.CategoryDao
import com.tfdev.inventorymanagement.data.dao.SupplierDao
import com.tfdev.inventorymanagement.data.entity.Category
import com.tfdev.inventorymanagement.data.entity.Product
import com.tfdev.inventorymanagement.data.entity.ProductDetails
import com.tfdev.inventorymanagement.data.entity.Supplier
import com.tfdev.inventorymanagement.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val categoryDao: CategoryDao,
    private val supplierDao: SupplierDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers: StateFlow<List<Supplier>> = _suppliers.asStateFlow()

    init {
        loadProducts()
        observeSearchQuery()
        loadCategories()
        loadSuppliers()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                categoryDao.getAllCategories().collect { categories ->
                    if (categories.isEmpty()) {
                        insertDefaultCategories()
                    }
                    _categories.value = categories
                }
            } catch (e: Exception) {
                Timber.e(e, "Kategoriler yüklenirken hata")
            }
        }
    }

    private suspend fun insertDefaultCategories() {
        try {
            val defaultCategories = listOf(
                Category(categoryId = 1, name = "Elektronik", description = "Elektronik ürünler"),
                Category(categoryId = 2, name = "Giyim", description = "Giyim ürünleri"),
                Category(categoryId = 3, name = "Gıda", description = "Gıda ürünleri"),
                Category(categoryId = 4, name = "Kozmetik", description = "Kozmetik ürünleri"),
                Category(categoryId = 5, name = "Ev & Yaşam", description = "Ev ve yaşam ürünleri")
            )
            defaultCategories.forEach { category ->
                try {
                    categoryDao.insertCategory(category)
                } catch (e: Exception) {
                    Timber.e(e, "Kategori eklenirken hata: ${category.name}")
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Varsayılan kategoriler eklenirken hata")
        }
    }

    private fun loadSuppliers() {
        viewModelScope.launch {
            try {
                supplierDao.getAllSuppliers().collect { suppliers ->
                    if (suppliers.isEmpty()) {
                        insertDefaultSuppliers()
                    }
                    _suppliers.value = suppliers
                }
            } catch (e: Exception) {
                Timber.e(e, "Tedarikçiler yüklenirken hata")
            }
        }
    }

    private suspend fun insertDefaultSuppliers() {
        try {
            val defaultSuppliers = listOf(
                Supplier(supplierId = 1, name = "ABC Elektronik", email = "abc@example.com", phone = "5551234567", address = "İstanbul"),
                Supplier(supplierId = 2, name = "XYZ Tekstil", email = "xyz@example.com", phone = "5559876543", address = "İzmir"),
                Supplier(supplierId = 3, name = "123 Gıda", email = "123@example.com", phone = "5553334444", address = "Ankara"),
                Supplier(supplierId = 4, name = "456 Kozmetik", email = "456@example.com", phone = "5555556666", address = "Bursa"),
                Supplier(supplierId = 5, name = "789 Market", email = "789@example.com", phone = "5557778888", address = "Antalya")
            )
            defaultSuppliers.forEach { supplier ->
                try {
                    supplierDao.insertSupplier(supplier)
                } catch (e: Exception) {
                    Timber.e(e, "Tedarikçi eklenirken hata: ${supplier.name}")
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Varsayılan tedarikçiler eklenirken hata")
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                repository.getAllProducts().collect { products ->
                    _uiState.value = UiState.Success(products)
                }
            } catch (e: Exception) {
                Timber.e(e, "Ürünler yüklenirken hata")
                _uiState.value = UiState.Error("Ürünler yüklenirken hata oluştu")
            }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery.collect { query ->
                if (query.isNotBlank()) {
                    searchProducts(query)
                } else {
                    loadProducts()
                }
            }
        }
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                repository.searchProducts(query).collect { products ->
                    _uiState.value = UiState.Success(products)
                }
            } catch (e: Exception) {
                Timber.e(e, "Ürün araması sırasında hata")
                _uiState.value = UiState.Error("Ürün araması sırasında hata oluştu")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.insertProduct(product)
                loadProducts()
            } catch (e: Exception) {
                Timber.e(e, "Ürün eklenirken hata")
                _uiState.value = UiState.Error("Ürün eklenirken hata oluştu")
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.updateProduct(product)
                loadProducts()
            } catch (e: Exception) {
                Timber.e(e, "Ürün güncellenirken hata")
                _uiState.value = UiState.Error("Ürün güncellenirken hata oluştu")
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.deleteProduct(product)
                loadProducts()
            } catch (e: Exception) {
                Timber.e(e, "Ürün silinirken hata")
                _uiState.value = UiState.Error("Ürün silinirken hata oluştu")
            }
        }
    }

    fun getProductDetails(productId: Int) {
        viewModelScope.launch {
            try {
                repository.getProductDetails(productId).collect { productDetails ->
                    _uiState.value = UiState.ProductDetailsSuccess(productDetails)
                }
            } catch (e: Exception) {
                Timber.e(e, "Ürün detayları yüklenirken hata")
                _uiState.value = UiState.Error("Ürün detayları yüklenirken hata oluştu")
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val products: List<Product>) : UiState()
        data class ProductDetailsSuccess(val productDetails: ProductDetails) : UiState()
        data class Error(val message: String) : UiState()
    }
} 