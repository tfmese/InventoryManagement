package com.tfdev.inventorymanagement.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "shipment_details",
    foreignKeys = [
        ForeignKey(
            entity = Shipment::class,
            parentColumns = ["shipmentId"],
            childColumns = ["shipmentId"],
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
data class ShipmentDetails(
    @PrimaryKey(autoGenerate = true)
    val shipmentDetailsId: Int = 0,
    val shipmentId: Int,
    val productId: Int,
    val quantity: Int
) 