package com.tfdev.inventorymanagement.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "warehouses")
data class Warehouse(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "warehouseId")
    val warehouseId: Int = 0,
    
    @ColumnInfo(name = "warehouseName")
    val name: String,
    
    @ColumnInfo(name = "address")
    val address: String,
    
    @ColumnInfo(name = "city")
    val city: String,
    
    @ColumnInfo(name = "capacity")
    val capacity: Int
) : Parcelable 