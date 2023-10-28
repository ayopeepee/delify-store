package com.swmpire.delifyit.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.repository.AuthRepository
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : FirestoreRepository {
    override suspend fun createStore(store: StoreModel): Flow<NetworkResult<Boolean>> {
        return flow {

            emit(NetworkResult.Loading())

            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    firebaseFirestore
                        .collection(STORES)
                        .document(currentStore.uid)
                        .set(store)
                        .await()
                    emit(NetworkResult.Success(true))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }

        }
    }

    override suspend fun updateStore(storeId: FirebaseUser): Flow<NetworkResult<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun addItem(item: ItemModel): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    item.storeReference = firebaseFirestore
                        .collection(STORES)
                        .document(currentStore.uid)
                    firebaseFirestore
                        .collection(ITEMS)
                        .add(item)
                        .await()
                    emit(NetworkResult.Success(true))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }
        }
    }

    companion object Constants {
        const val STORES = "stores"
        const val ITEMS = "items"
    }
}