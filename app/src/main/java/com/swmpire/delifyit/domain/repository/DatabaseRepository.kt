package com.swmpire.delifyit.domain.repository

import com.swmpire.delifyit.data.room.ItemEntity
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun updateSelectStatus(id: String, isSelected: Boolean)
    suspend fun getSelectedItemsCount() : Int
    suspend fun getItemsCount() : Int
    suspend fun deselectAllItems()
    suspend fun isItemSelected(id: String) : Boolean
}