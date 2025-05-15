package com.tfdev.inventorymanagement.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "inventory_transactions",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Warehouse::class,
            parentColumns = ["warehouseId"],
            childColumns = ["warehouseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class InventoryTransaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,
    val productId: Int,
    val warehouseId: Int,
    val quantity: Int,
    val transactionDirection: String, // "IN" veya "OUT"
    val transactionDate: Date,
    val description: String? = null
) 