package com.tfdev.inventorymanagement.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tfdev.inventorymanagement.data.converter.Converters
import com.tfdev.inventorymanagement.data.dao.CategoryDao
import com.tfdev.inventorymanagement.data.dao.CustomerDao
import com.tfdev.inventorymanagement.data.dao.InventoryTransactionDao
import com.tfdev.inventorymanagement.data.dao.OrderDao
import com.tfdev.inventorymanagement.data.dao.OrderDetailsDao
import com.tfdev.inventorymanagement.data.dao.ProductDao
import com.tfdev.inventorymanagement.data.dao.ShipmentDao
import com.tfdev.inventorymanagement.data.dao.ShipmentDetailsDao
import com.tfdev.inventorymanagement.data.dao.SupplierDao
import com.tfdev.inventorymanagement.data.dao.WarehouseDao
import com.tfdev.inventorymanagement.data.dao.WarehouseStockDao
import com.tfdev.inventorymanagement.data.entity.Category
import com.tfdev.inventorymanagement.data.entity.Customer
import com.tfdev.inventorymanagement.data.entity.InventoryTransaction
import com.tfdev.inventorymanagement.data.entity.Order
import com.tfdev.inventorymanagement.data.entity.OrderDetails
import com.tfdev.inventorymanagement.data.entity.Product
import com.tfdev.inventorymanagement.data.entity.Shipment
import com.tfdev.inventorymanagement.data.entity.ShipmentDetails
import com.tfdev.inventorymanagement.data.entity.Supplier
import com.tfdev.inventorymanagement.data.entity.Warehouse
import com.tfdev.inventorymanagement.data.entity.WarehouseStock
import timber.log.Timber

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
    version = 3,
    exportSchema = false
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

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Eski tabloyu yedekle
                db.execSQL("CREATE TABLE warehouses_backup (warehouseId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, warehouseName TEXT NOT NULL, address TEXT NOT NULL, city TEXT NOT NULL, capacity INTEGER NOT NULL)")
                db.execSQL("INSERT INTO warehouses_backup (warehouseId, warehouseName, address, city, capacity) SELECT warehouseId, name, address, city, capacity FROM warehouses")
                
                // Eski tabloyu sil
                db.execSQL("DROP TABLE warehouses")
                
                // Yeni tabloyu oluştur
                db.execSQL("ALTER TABLE warehouses_backup RENAME TO warehouses")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Tüm tabloları temizle
                db.execSQL("DELETE FROM products")
                db.execSQL("DELETE FROM categories")
                db.execSQL("DELETE FROM suppliers")
                db.execSQL("DELETE FROM warehouses")
                db.execSQL("DELETE FROM orders")
                db.execSQL("DELETE FROM order_details")
                db.execSQL("DELETE FROM shipments")
                db.execSQL("DELETE FROM shipment_details")
                db.execSQL("DELETE FROM warehouse_stocks")
                db.execSQL("DELETE FROM inventory_transactions")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inventory_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
