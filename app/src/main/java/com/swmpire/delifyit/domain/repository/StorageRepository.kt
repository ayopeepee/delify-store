package com.swmpire.delifyit.domain.repository

import android.net.Uri
import com.swmpire.delifyit.domain.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    suspend fun addItemImage(uri: Uri) : Flow<NetworkResult<String>>
}