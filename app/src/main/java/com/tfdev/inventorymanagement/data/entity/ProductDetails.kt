package com.tfdev.inventorymanagement.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProductDetails(
    @Embedded
    val product: Product,
    
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: Category,
    
    @Relation(
        parentColumn = "supplierId",
        entityColumn = "supplierId"
    )
    val supplier: Supplier
) 