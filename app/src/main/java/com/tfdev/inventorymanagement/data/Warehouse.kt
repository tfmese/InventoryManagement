package com.tfdev.inventorymanagement.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warehouses")
data class Warehouse(
    @PrimaryKey(autoGenerate = true)
    val warehouseId: Int = 0,
    val warehouseName: String,
    val address: String,
    val capacity: Int,
    val city: String
) 