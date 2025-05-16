package com.tfdev.inventorymanagement

import android.app.Application
import com.tfdev.inventorymanagement.data.DatabaseSeeder
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class InventoryApplication : Application() {

    @Inject
    lateinit var databaseSeeder: DatabaseSeeder

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Varsayılan verileri ekle
        databaseSeeder.seedDatabase()
    }
} 