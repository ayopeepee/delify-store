package com.swmpire.delifyit.data.room

import android.util.Log
import com.swmpire.delifyit.domain.repository.DatabaseRepository
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val itemDao: ItemDao
) : DatabaseRepository {
    override suspend fun updateSelectStatus(id: String, isSelected: Boolean) {
        itemDao.updateSelectStatus(id, isSelected)
        Log.d("TAG", "updateSelectStatus(Repository): $id $isSelected")
    }

    override suspend fun getSelectedItemsCount(): Int = itemDao.getSelectedItemsCount()

    override suspend fun getItemsCount(): Int = itemDao.getAllItemsCount()
    override suspend fun deselectAllItems() = itemDao.deselectAllItems()
}