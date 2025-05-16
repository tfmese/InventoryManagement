package com.tfdev.inventorymanagement.data.dao

import androidx.room.*
import com.tfdev.inventorymanagement.data.entity.Warehouse
import kotlinx.coroutines.flow.Flow

@Dao
interface WarehouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarehouse(warehouse: Warehouse)

    @Update
    suspend fun update(warehouse: Warehouse)

    @Delete
    suspend fun delete(warehouse: Warehouse)

    @Query("SELECT * FROM warehouses")
    fun getAllWarehouses(): Flow<List<Warehouse>>

    @Query("SELECT * FROM warehouses WHERE warehouseId = :id")
    suspend fun getWarehouseById(id: Int): Warehouse?

    @Query("SELECT * FROM warehouses WHERE warehouseName LIKE '%' || :searchQuery || '%' OR city LIKE '%' || :searchQuery || '%'")
    fun searchWarehouses(searchQuery: String): Flow<List<Warehouse>>

    @Query("SELECT * FROM warehouses WHERE capacity >= :minCapacity")
    fun getWarehousesWithAvailableCapacity(minCapacity: Int): Flow<List<Warehouse>>

    @Query("DELETE FROM warehouses")
    suspend fun deleteAllWarehouses()
} 