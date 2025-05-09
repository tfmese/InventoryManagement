package com.tfdev.inventorymanagement.data

import androidx.room.*

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Query("SELECT * FROM products")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM products WHERE productId = :id")
    suspend fun getById(id: Int): Product?

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)
}
