package com.tfdev.inventorymanagement.data

import com.tfdev.inventorymanagement.data.dao.CategoryDao
import com.tfdev.inventorymanagement.data.dao.SupplierDao
import com.tfdev.inventorymanagement.data.dao.WarehouseDao
import com.tfdev.inventorymanagement.data.entity.Category
import com.tfdev.inventorymanagement.data.entity.Supplier
import com.tfdev.inventorymanagement.data.entity.Warehouse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor(
    private val categoryDao: CategoryDao,
    private val supplierDao: SupplierDao,
    private val warehouseDao: WarehouseDao
) {
    fun seedDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            // Kategorileri ekle
            val categories = listOf(
                Category(categoryId = 1, name = "Elektronik", description = "Elektronik ürünler"),
                Category(categoryId = 2, name = "Giyim", description = "Giyim ürünleri"),
                Category(categoryId = 3, name = "Gıda", description = "Gıda ürünleri"),
                Category(categoryId = 4, name = "Kozmetik", description = "Kozmetik ürünleri"),
                Category(categoryId = 5, name = "Ev & Yaşam", description = "Ev ve yaşam ürünleri")
            )
            categories.forEach { category ->
                categoryDao.insertCategory(category)
            }

            // Tedarikçileri ekle
            val suppliers = listOf(
                Supplier(supplierId = 1, name = "ABC Elektronik", email = "abc@example.com", phone = "5551234567", address = "İstanbul"),
                Supplier(supplierId = 2, name = "XYZ Tekstil", email = "xyz@example.com", phone = "5559876543", address = "İzmir"),
                Supplier(supplierId = 3, name = "123 Gıda", email = "123@example.com", phone = "5553334444", address = "Ankara"),
                Supplier(supplierId = 4, name = "456 Kozmetik", email = "456@example.com", phone = "5555556666", address = "Bursa"),
                Supplier(supplierId = 5, name = "789 Market", email = "789@example.com", phone = "5557778888", address = "Antalya")
            )
            suppliers.forEach { supplier ->
                supplierDao.insertSupplier(supplier)
            }

            // Depoları ekle
            val warehouses = listOf(
                Warehouse(warehouseId = 1, name = "Ana Depo", address = "Atatürk Mah. 1234 Sok. No:1", city = "İstanbul", capacity = 1000),
                Warehouse(warehouseId = 2, name = "İzmir Depo", address = "Cumhuriyet Mah. 567 Sok. No:2", city = "İzmir", capacity = 500),
                Warehouse(warehouseId = 3, name = "Ankara Depo", address = "Kızılay Mah. 890 Sok. No:3", city = "Ankara", capacity = 750)
            )
            warehouses.forEach { warehouse ->
                warehouseDao.insertWarehouse(warehouse)
            }
        }
    }
} 