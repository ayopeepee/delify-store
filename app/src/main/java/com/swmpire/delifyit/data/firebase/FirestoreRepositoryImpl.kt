package com.swmpire.delifyit.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import com.swmpire.delifyit.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : FirestoreRepository {
    override suspend fun createStore(store: StoreModel): Flow<NetworkResult<Boolean>> {
        return flow {

            emit(NetworkResult.Loading())

            try {
                if (store.storeId != null) {
                    firebaseFirestore
                        .collection(Constants.STORES)
                        .document(store.storeId)
                        .set(store)
                        .await()
                }

                emit(NetworkResult.Success(true))
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }

        }
    }

    override suspend fun updateStore(store: StoreModel): Flow<NetworkResult<Boolean>> {
        TODO("Not yet implemented")
    }
}