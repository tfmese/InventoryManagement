package com.tfdev.inventorymanagement.ui.customer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tfdev.inventorymanagement.databinding.ActivityCustomerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Müşteriler"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 