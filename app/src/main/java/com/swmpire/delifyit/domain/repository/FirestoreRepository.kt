package com.swmpire.delifyit.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend fun createStore(store: StoreModel) : Flow<NetworkResult<Boolean>>
    suspend fun updateStore(storeId: FirebaseUser) : Flow<NetworkResult<Boolean>>
    suspend fun addItem(item: ItemModel) : Flow<NetworkResult<Boolean>>
    suspend fun getAllItems() : Flow<NetworkResult<List<ItemModel>>>
    fun getItemsCallback() : Flow<List<ItemModel>>
    suspend fun updateItem(item: ItemModel) : Flow<NetworkResult<Boolean>>
    suspend fun deleteSelectedItems() : Flow<NetworkResult<Boolean>>
    suspend fun getStore() : Flow<NetworkResult<StoreModel>>
}