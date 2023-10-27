package com.swmpire.delifyit.domain.repository

import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend fun createStore(store: StoreModel) : Flow<NetworkResult<Boolean>>
    suspend fun updateStore(store: StoreModel) : Flow<NetworkResult<Boolean>>
}