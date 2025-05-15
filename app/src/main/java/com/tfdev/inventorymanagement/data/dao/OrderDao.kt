package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.Order
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: Order): Long // Returns the inserted order's ID

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)

    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE orderId = :id")
    suspend fun getOrderById(id: Int): Order?

    @Query("SELECT * FROM orders WHERE customerId = :customerId")
    fun getOrdersByCustomer(customerId: Int): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE orderDate BETWEEN :startDate AND :endDate")
    fun getOrdersByDateRange(startDate: Date, endDate: Date): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE deliveryDate IS NULL")
    fun getPendingDeliveries(): Flow<List<Order>>

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
} 