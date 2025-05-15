package com.tfdev.inventorymanagement.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_details",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
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
data class OrderDetails(
    @PrimaryKey(autoGenerate = true)
    val orderDetailsId: Int = 0,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val unitPrice: Double
) 