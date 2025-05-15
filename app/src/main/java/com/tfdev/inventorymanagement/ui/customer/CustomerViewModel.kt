package com.tfdev.inventorymanagement.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfdev.inventorymanagement.data.Customer
import com.tfdev.inventorymanagement.data.dao.CustomerDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerDao: CustomerDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadCustomers()
    }

    fun loadCustomers() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                customerDao.getAllCustomers().collectLatest { customers ->
                    _uiState.value = UiState.Success(customers)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Müşteriler yüklenirken bir hata oluştu")
            }
        }
    }

    fun addCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                customerDao.insert(customer)
                loadCustomers()
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Müşteri eklenirken bir hata oluştu")
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val customers: List<Customer>) : UiState()
        data class Error(val message: String) : UiState()
    }
} 