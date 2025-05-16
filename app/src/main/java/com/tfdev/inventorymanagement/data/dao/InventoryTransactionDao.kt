package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.entity.InventoryTransaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface InventoryTransactionDao {
    @Query("SELECT * FROM inventory_transactions WHERE productId = :productId")
    fun getTransactionsByProduct(productId: Int): Flow<List<InventoryTransaction>>

    @Query("SELECT * FROM inventory_transactions WHERE warehouseId = :warehouseId")
    fun getTransactionsByWarehouse(warehouseId: Int): Flow<List<InventoryTransaction>>

    @Query("SELECT * FROM inventory_transactions WHERE transactionDate BETWEEN :startDate AND :endDate")
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<InventoryTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: InventoryTransaction)

    @Update
    suspend fun updateTransaction(transaction: InventoryTransaction)

    @Delete
    suspend fun deleteTransaction(transaction: InventoryTransaction)

    @Query("DELETE FROM inventory_transactions WHERE productId = :productId")
    suspend fun deleteTransactionsByProduct(productId: Int)

    @Query("DELETE FROM inventory_transactions WHERE warehouseId = :warehouseId")
    suspend fun deleteTransactionsByWarehouse(warehouseId: Int)
} 