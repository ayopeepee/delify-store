package com.swmpire.delifyit.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String?,
    val description: String?,
    val category: String?,
    val price: Int?,
    val imageUrl: String?,
    var isSelected: Boolean = false
)
