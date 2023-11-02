package com.swmpire.delifyit.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.repository.AuthRepository
import com.swmpire.delifyit.domain.repository.FirestoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
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
                    item.id = UUID.randomUUID().toString()
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

    override suspend fun getAllItems(): Flow<NetworkResult<List<ItemModel>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    val storeReference =
                        firebaseFirestore.collection(STORES).document(currentStore.uid)
                    Log.d("TAG", "getAllItems(store reference): ${storeReference.path}")
                    val querySnapshot = firebaseFirestore.collection(ITEMS)
                        .whereEqualTo(STORE_REFERENCE, storeReference)
                        .get()
                        .await()
                        .documents

                    if (querySnapshot.isNotEmpty()) {
                        emit(NetworkResult.Success(querySnapshot.mapNotNull { it.toObject<ItemModel>() }))
                    } else {
                        emit(NetworkResult.Error("nothing to show"))
                    }
                } else {
                    emit(NetworkResult.Error("user not logged in"))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }
        }
    }

    override fun getItemsCallback(): Flow<List<ItemModel>> {
        return callbackFlow {
            val currentStore = firebaseAuth.currentUser
            if (currentStore != null) {
                val storeReference = firebaseFirestore.collection(STORES).document(currentStore.uid)
                val listener = firebaseFirestore.collection(ITEMS)
                    .whereEqualTo(STORE_REFERENCE, storeReference)
                    .addSnapshotListener { value, error ->
                        if (error != null) close(error)
                        if (value != null) {
                            Log.d("TAG", "getItemsCallback: $value")
                            trySend(value.toObjects<ItemModel>())
                        }
                }
                awaitClose { listener.remove() }
            }
        }
    }

    companion object Constants {
        const val STORES = "stores"
        const val ITEMS = "items"
        const val STORE_REFERENCE = "storeReference"
    }
}