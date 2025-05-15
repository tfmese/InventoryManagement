package com.tfdev.inventorymanagement.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "warehouse_stocks",
    primaryKeys = ["warehouseId", "productId"],
    foreignKeys = [
        ForeignKey(
            entity = Warehouse::class,
            parentColumns = ["warehouseId"],
            childColumns = ["warehouseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WarehouseStock(
    val warehouseId: Int,
    val productId: Int,
    val stockQuantity: Int
) 