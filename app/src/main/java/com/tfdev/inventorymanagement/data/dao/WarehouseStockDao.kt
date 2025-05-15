package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.WarehouseStock
import kotlinx.coroutines.flow.Flow

@Dao
interface WarehouseStockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(warehouseStock: WarehouseStock)

    @Update
    suspend fun update(warehouseStock: WarehouseStock)

    @Delete
    suspend fun delete(warehouseStock: WarehouseStock)

    @Query("SELECT * FROM warehouse_stocks WHERE warehouseId = :warehouseId")
    fun getStocksByWarehouse(warehouseId: Int): Flow<List<WarehouseStock>>

    @Query("SELECT * FROM warehouse_stocks WHERE productId = :productId")
    fun getStocksByProduct(productId: Int): Flow<List<WarehouseStock>>

    @Query("SELECT * FROM warehouse_stocks WHERE warehouseId = :warehouseId AND productId = :productId")
    suspend fun getSpecificStock(warehouseId: Int, productId: Int): WarehouseStock?

    @Query("SELECT SUM(stockQuantity) FROM warehouse_stocks WHERE productId = :productId")
    suspend fun getTotalStockForProduct(productId: Int): Int?

    @Query("SELECT * FROM warehouse_stocks WHERE stockQuantity < :threshold")
    fun getLowStockItems(threshold: Int): Flow<List<WarehouseStock>>

    @Query("DELETE FROM warehouse_stocks WHERE warehouseId = :warehouseId")
    suspend fun deleteStocksByWarehouse(warehouseId: Int)

    @Query("DELETE FROM warehouse_stocks")
    suspend fun deleteAllStocks()
} 