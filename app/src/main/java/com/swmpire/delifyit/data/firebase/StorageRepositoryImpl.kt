package com.swmpire.delifyit.data.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) : StorageRepository {

    override suspend fun addItemImage(uri: Uri): Flow<NetworkResult<String>> {
        return flow {

            emit(NetworkResult.Loading())

            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    val reference = firebaseStorage.getReference(IMAGES)
                        .child("${currentStore.uid}/${UUID.randomUUID()}")
                    val uploadTask = reference.putFile(uri).await()
                    val downloadUrl = uploadTask.metadata?.reference?.downloadUrl?.await()

                    if (downloadUrl != null) {
                        Log.d(TAG, "addItemImage(URL): $downloadUrl")
                        emit(NetworkResult.Success(downloadUrl.toString()))
                    } else {
                        emit(NetworkResult.Error("something went wrong while uploading image"))
                    }
                } else {
                    emit(NetworkResult.Error("not logged in"))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }
        }
    }

    companion object Constants {
        const val TAG = "StorageRepositoryImpl"
        const val IMAGES = "images"
    }
}