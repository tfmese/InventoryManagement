package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert
    suspend fun insert(customer: Customer)

    @Update
    suspend fun update(customer: Customer)

    @Delete
    suspend fun delete(customer: Customer)

    @Query("SELECT * FROM customers")
    fun getAllCustomers(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE customerId = :id")
    suspend fun getCustomerById(id: Int): Customer?

    @Query("SELECT * FROM customers WHERE name LIKE '%' || :searchQuery || '%' OR city LIKE '%' || :searchQuery || '%'")
    fun searchCustomers(searchQuery: String): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE city = :city")
    fun getCustomersByCity(city: String): Flow<List<Customer>>

    @Query("DELETE FROM customers")
    suspend fun deleteAllCustomers()
} 