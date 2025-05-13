package com.tfdev.inventorymanagement.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val customerId: Int = 0,
    val name: String,
    val address: String,
    val phoneNumber: String,
    val email: String,
    val city: String
)
