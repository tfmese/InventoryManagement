package com.tfdev.inventorymanagement.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val productId: Int = 0,
    val name: String,
    val description: String,
    val stock: Int,
    val price: Double,
    val categoryId: Int,     // Şimdilik sadece ID olarak tutuyoruz
    val supplierId: Int
)
