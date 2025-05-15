package com.tfdev.inventorymanagement.repository

import com.tfdev.inventorymanagement.data.Product
import com.tfdev.inventorymanagement.data.WarehouseStockInfo
import com.tfdev.inventorymanagement.data.InventoryTransaction
import com.tfdev.inventorymanagement.data.dao.ProductDao
import com.tfdev.inventorymanagement.data.dao.WarehouseDao
import com.tfdev.inventorymanagement.data.dao.WarehouseStockDao
import com.tfdev.inventorymanagement.data.dao.InventoryTransactionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val warehouseDao: WarehouseDao,
    private val warehouseStockDao: WarehouseStockDao,
    private val inventoryTransactionDao: InventoryTransactionDao
) {
    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()
    
    fun searchProducts(query: String): Flow<List<Product>> = productDao.searchProducts(query)
    
    fun getProductsByCategory(categoryId: Int): Flow<List<Product>> = productDao.getProductsByCategory(categoryId)
    
    fun getLowStockProducts(threshold: Int): Flow<List<Product>> = productDao.getLowStockProducts(threshold)
    
    fun getProductById(id: Int): Flow<Product?> = flow {
        emit(productDao.getProductById(id))
    }
    
    suspend fun insertProduct(product: Product) = productDao.insert(product)
    
    suspend fun updateProduct(product: Product) = productDao.update(product)
    
    suspend fun deleteProduct(product: Product) = productDao.delete(product)

    fun getWarehouseStocksForProduct(productId: Int): Flow<List<WarehouseStockInfo>> {
        return warehouseStockDao.getStocksByProduct(productId).map { stocks ->
            stocks.map { stock ->
                val warehouse = warehouseDao.getWarehouseById(stock.warehouseId)
                WarehouseStockInfo(
                    warehouseId = stock.warehouseId,
                    warehouseName = warehouse?.warehouseName ?: "Bilinmeyen Depo",
                    stockQuantity = stock.stockQuantity
                )
            }
        }
    }

    fun getTransactionsForProduct(productId: Int): Flow<List<InventoryTransaction>> {
        return inventoryTransactionDao.getTransactionsByProduct(productId)
    }
} 