package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.Supplier
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {
    @Insert
    suspend fun insert(supplier: Supplier)

    @Update
    suspend fun update(supplier: Supplier)

    @Delete
    suspend fun delete(supplier: Supplier)

    @Query("SELECT * FROM suppliers")
    fun getAllSuppliers(): Flow<List<Supplier>>

    @Query("SELECT * FROM suppliers WHERE supplierId = :id")
    suspend fun getSupplierById(id: Int): Supplier?

    @Query("SELECT * FROM suppliers WHERE name LIKE '%' || :searchQuery || '%' OR address LIKE '%' || :searchQuery || '%' OR email LIKE '%' || :searchQuery || '%' OR phone LIKE '%' || :searchQuery || '%'")
    fun searchSuppliers(searchQuery: String): Flow<List<Supplier>>

    @Query("DELETE FROM suppliers")
    suspend fun deleteAllSuppliers()
} 