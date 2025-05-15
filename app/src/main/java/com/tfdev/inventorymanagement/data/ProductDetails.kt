package com.tfdev.inventorymanagement.data

data class ProductDetails(
    val product: Product,
    val warehouseStocks: List<WarehouseStockInfo>,
    val transactions: List<InventoryTransaction>
)

data class WarehouseStockInfo(
    val warehouseId: Int,
    val warehouseName: String,
    val stockQuantity: Int
) 