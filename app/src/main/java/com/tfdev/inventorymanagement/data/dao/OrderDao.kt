package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.entity.Order
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE orderId = :id")
    suspend fun getOrderById(id: Int): Order?

    @Query("SELECT * FROM orders WHERE customerId = :customerId")
    fun getOrdersByCustomer(customerId: Int): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE orderDate BETWEEN :startDate AND :endDate")
    fun getOrdersByDateRange(startDate: Date, endDate: Date): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE status = :status")
    fun getOrdersByStatus(status: String): Flow<List<Order>>

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
} 