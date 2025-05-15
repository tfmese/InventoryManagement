package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.OrderDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDetailsDao {
    @Insert
    suspend fun insert(orderDetails: OrderDetails)

    @Insert
    suspend fun insertAll(orderDetails: List<OrderDetails>)

    @Update
    suspend fun update(orderDetails: OrderDetails)

    @Delete
    suspend fun delete(orderDetails: OrderDetails)

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    fun getOrderDetailsByOrderId(orderId: Int): Flow<List<OrderDetails>>

    @Query("SELECT * FROM order_details WHERE productId = :productId")
    fun getOrderDetailsByProductId(productId: Int): Flow<List<OrderDetails>>

    @Query("SELECT SUM(quantity * unitPrice) FROM order_details WHERE orderId = :orderId")
    suspend fun getOrderTotal(orderId: Int): Double?

    @Query("DELETE FROM order_details WHERE orderId = :orderId")
    suspend fun deleteOrderDetailsByOrderId(orderId: Int)

    @Query("DELETE FROM order_details")
    suspend fun deleteAllOrderDetails()
} 