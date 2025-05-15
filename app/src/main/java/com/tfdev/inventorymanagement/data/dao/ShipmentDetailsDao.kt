package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.ShipmentDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ShipmentDetailsDao {
    @Insert
    suspend fun insert(shipmentDetails: ShipmentDetails)

    @Insert
    suspend fun insertAll(shipmentDetails: List<ShipmentDetails>)

    @Update
    suspend fun update(shipmentDetails: ShipmentDetails)

    @Delete
    suspend fun delete(shipmentDetails: ShipmentDetails)

    @Query("SELECT * FROM shipment_details WHERE shipmentId = :shipmentId")
    fun getShipmentDetailsByShipmentId(shipmentId: Int): Flow<List<ShipmentDetails>>

    @Query("SELECT * FROM shipment_details WHERE productId = :productId")
    fun getShipmentDetailsByProductId(productId: Int): Flow<List<ShipmentDetails>>

    @Query("SELECT SUM(quantity) FROM shipment_details WHERE shipmentId = :shipmentId")
    suspend fun getTotalQuantityByShipment(shipmentId: Int): Int?

    @Query("DELETE FROM shipment_details WHERE shipmentId = :shipmentId")
    suspend fun deleteShipmentDetailsByShipmentId(shipmentId: Int)

    @Query("DELETE FROM shipment_details")
    suspend fun deleteAllShipmentDetails()
} 