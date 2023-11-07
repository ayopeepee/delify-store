package com.swmpire.delifyit.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items")
    suspend fun getAllItems(): List<ItemEntity>

    @Query("SELECT COUNT(*) FROM items")
    suspend fun getAllItemsCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>)

    @Query("UPDATE items SET isSelected = :isSelected WHERE id = :id")
    suspend fun updateSelectStatus(id: String, isSelected: Boolean)

    @Query("SELECT * FROM items WHERE isSelected = 1")
    suspend fun getSelectedItems() : List<ItemEntity>

    @Query("SELECT COUNT(*) FROM items WHERE isSelected = 1")
    suspend fun getSelectedItemsCount() : Int

    @Query("UPDATE items SET isSelected = 0")
    suspend fun deselectAllItems()

    @Query("DELETE FROM items WHERE isSelected = 1")
    suspend fun deleteAllSelectedItems()

    @Query("DELETE FROM items")
    suspend fun nukeTable()
}