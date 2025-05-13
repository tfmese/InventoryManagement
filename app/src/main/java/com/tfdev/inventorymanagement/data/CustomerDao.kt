package com.tfdev.inventorymanagement.data

import androidx.room.*


@Dao
interface CustomerDao {
    @Insert
    suspend fun insert(customer: Customer)

    @Query("SELECT * FROM customers")
    suspend fun getAll(): List<Customer>
}
