package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.InventoryTransaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface InventoryTransactionDao {
    @Insert
    suspend fun insert(transaction: InventoryTransaction)

    @Update
    suspend fun update(transaction: InventoryTransaction)

    @Delete
    suspend fun delete(transaction: InventoryTransaction)

    @Query("SELECT * FROM inventory_transactions")
    fun getAllTransactions(): Flow<List<InventoryTransaction>>

    @Query("SELECT * FROM inventory_transactions WHERE transactionId = :id")
    suspend fun getTransactionById(id: Int): InventoryTransaction?

    @Query("SELECT * FROM inventory_transactions WHERE productId = :productId")
    fun getTransactionsByProduct(productId: Int): Flow<List<InventoryTransaction>>

    @Query("SELECT * FROM inventory_transactions WHERE warehouseId = :warehouseId")
    fun getTransactionsByWarehouse(warehouseId: Int): Flow<List<InventoryTransaction>>

    @Query("SELECT * FROM inventory_transactions WHERE transactionDate BETWEEN :startDate AND :endDate")
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<InventoryTransaction>>

    @Query("SELECT * FROM inventory_transactions WHERE transactionDirection = :direction")
    fun getTransactionsByDirection(direction: String): Flow<List<InventoryTransaction>>

    @Query("SELECT SUM(CASE WHEN transactionDirection = 'IN' THEN quantity ELSE -quantity END) FROM inventory_transactions WHERE productId = :productId")
    suspend fun getNetStockChangeForProduct(productId: Int): Int?

    @Query("DELETE FROM inventory_transactions")
    suspend fun deleteAllTransactions()
} 