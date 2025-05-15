package com.tfdev.inventorymanagement.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tfdev.inventorymanagement.data.dao.*

@Database(
    entities = [
        Product::class,
        Customer::class,
        Category::class,
        Supplier::class,
        Warehouse::class,
        Order::class,
        OrderDetails::class,
        Shipment::class,
        ShipmentDetails::class,
        WarehouseStock::class,
        InventoryTransaction::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun customerDao(): CustomerDao
    abstract fun categoryDao(): CategoryDao
    abstract fun supplierDao(): SupplierDao
    abstract fun warehouseDao(): WarehouseDao
    abstract fun orderDao(): OrderDao
    abstract fun orderDetailsDao(): OrderDetailsDao
    abstract fun shipmentDao(): ShipmentDao
    abstract fun shipmentDetailsDao(): ShipmentDetailsDao
    abstract fun warehouseStockDao(): WarehouseStockDao
    abstract fun inventoryTransactionDao(): InventoryTransactionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inventory_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
