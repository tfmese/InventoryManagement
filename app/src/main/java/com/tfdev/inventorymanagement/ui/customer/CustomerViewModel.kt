package com.tfdev.inventorymanagement.ui.customer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tfdev.inventorymanagement.data.AppDatabase
import com.tfdev.inventorymanagement.data.dao.CustomerDao
import com.tfdev.inventorymanagement.data.entity.Customer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CustomerViewModel(application: Application) : AndroidViewModel(application) {
    private val customerDao: CustomerDao
    val allCustomers: LiveData<List<Customer>>
        get() = _allCustomers
    private val _allCustomers = MutableLiveData<List<Customer>>()

    val searchResults: LiveData<List<Customer>>
        get() = _searchResults
    private val _searchResults = MutableLiveData<List<Customer>>()

    private var editingCustomer: Customer? = null

    init {
        val database = AppDatabase.getDatabase(application)
        customerDao = database.customerDao()
        loadCustomers()
    }

    private fun loadCustomers() {
        viewModelScope.launch(Dispatchers.IO) {
            customerDao.getAllCustomers().collect { customers ->
                _allCustomers.postValue(customers)
            }
        }
    }

    fun saveCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            if (editingCustomer != null) {
                customerDao.updateCustomer(customer.copy(customerId = editingCustomer!!.customerId))
            } else {
                customerDao.insertCustomer(customer)
            }
            loadCustomers()
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            customerDao.deleteCustomer(customer)
            loadCustomers()
        }
    }

    fun searchCustomers(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            customerDao.searchCustomers(query).collect { customers ->
                _searchResults.postValue(customers)
            }
        }
    }

    fun setEditingCustomer(customer: Customer) {
        editingCustomer = customer
    }

    fun clearEditingCustomer() {
        editingCustomer = null
    }
} 