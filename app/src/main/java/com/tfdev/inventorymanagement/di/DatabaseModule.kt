package com.tfdev.inventorymanagement.di

import android.content.Context
import com.tfdev.inventorymanagement.data.AppDatabase
import com.tfdev.inventorymanagement.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideCustomerDao(database: AppDatabase): CustomerDao {
        return database.customerDao()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideSupplierDao(database: AppDatabase): SupplierDao {
        return database.supplierDao()
    }

    @Provides
    fun provideWarehouseDao(database: AppDatabase): WarehouseDao {
        return database.warehouseDao()
    }

    @Provides
    fun provideOrderDao(database: AppDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    fun provideOrderDetailsDao(database: AppDatabase): OrderDetailsDao {
        return database.orderDetailsDao()
    }

    @Provides
    fun provideShipmentDao(database: AppDatabase): ShipmentDao {
        return database.shipmentDao()
    }

    @Provides
    fun provideShipmentDetailsDao(database: AppDatabase): ShipmentDetailsDao {
        return database.shipmentDetailsDao()
    }

    @Provides
    fun provideWarehouseStockDao(database: AppDatabase): WarehouseStockDao {
        return database.warehouseStockDao()
    }

    @Provides
    fun provideInventoryTransactionDao(database: AppDatabase): InventoryTransactionDao {
        return database.inventoryTransactionDao()
    }
} 