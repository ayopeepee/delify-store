package com.swmpire.delifyit.data.firebase

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.swmpire.delifyit.domain.uitl.DateFilter
import com.swmpire.delifyit.data.mapper.EntityMapperImpl
import com.swmpire.delifyit.data.room.ItemDao
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.OrderModel
import com.swmpire.delifyit.domain.model.StoreModel
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
    private val firebaseFirestore: FirebaseFirestore,
    private val itemDao: ItemDao
) : FirestoreRepository {
    override suspend fun createStore(store: StoreModel): Flow<NetworkResult<Boolean>> {
        return flow {

            emit(NetworkResult.Loading())

            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    store.isVerified = false
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


    override suspend fun addItem(item: ItemModel): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    val id = UUID.randomUUID().toString()
                    item.id = id
                    item.storeReference = firebaseFirestore
                        .collection(STORES)
                        .document(currentStore.uid)
                    firebaseFirestore
                        .collection(ITEMS)
                        .document(id)
                        .set(item)
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
                        val items = querySnapshot.mapNotNull { it.toObject<ItemModel>() }
                        itemDao.nukeTable()
                        itemDao.insertItems(items = items.map {
                            EntityMapperImpl.mapFromModelToEntity(
                                it
                            )
                        })
                        emit(NetworkResult.Success(items))
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
                            trySend(value.toObjects<ItemModel>())
                        }
                    }
                awaitClose { listener.remove() }
            }
        }
    }

    override suspend fun updateItem(item: ItemModel): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val itemId = item.id
                if (itemId != null) {
                    val itemReference = firebaseFirestore.collection(ITEMS).document(itemId)
                    val updates = mutableMapOf<String, Any>()
                    with(item) {
                        name?.let { updates[NAME] = it }
                        description?.let { updates[DESCRIPTION] = it }
                        category?.let { updates[CATEGORY] = it }
                        price?.let { updates[PRICE] = it }
                        imageUrl?.let { updates[IMAGE_URL] = it }
                    }
                    if (updates.isNotEmpty()) {
                        itemReference.update(updates).await()
                        emit(NetworkResult.Success(true))
                    } else {
                        emit(NetworkResult.Error("nothing to update"))
                    }
                } else {
                    emit(NetworkResult.Error("id is null"))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }
        }
    }

    override suspend fun deleteSelectedItems(): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                itemDao.getSelectedItems().forEach { itemEntity ->
                    firebaseFirestore.collection(ITEMS).document(itemEntity.id).delete()
                }
                itemDao.deleteAllSelectedItems()
                emit(NetworkResult.Success(true))
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }
        }
    }

    override suspend fun getStore(): Flow<NetworkResult<StoreModel>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val currentStore = firebaseAuth.currentUser?.uid
                val snapshot = firebaseFirestore
                    .collection(STORES)
                    .document(currentStore.toString())
                    .get()
                    .await()

                val store = snapshot.toObject<StoreModel>()
                if (store != null) {
                    emit(NetworkResult.Success(store))
                } else {
                    emit(NetworkResult.Error(message = "can't get store"))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }
        }
    }

    override suspend fun updateStore(store: StoreModel): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val currentStore = firebaseAuth.currentUser?.uid
                if (currentStore != null) {
                    val storeReference =
                        firebaseFirestore.collection(STORES).document(currentStore.toString())
                    val updates = mutableMapOf<String, Any>()
                    with(store) {
                        name?.let { updates[NAME] = it }
                        description?.let { updates[DESCRIPTION] = it }
                        type?.let { updates[TYPE] = it }
                        address?.let { updates[ADDRESS] = it }
                        profilePictureUrl?.let { updates[PROFILE_PICTURE_URL] = it }
                    }
                    if (updates.isNotEmpty()) {
                        storeReference.update(updates).await()
                        emit(NetworkResult.Success(true))
                    } else {
                        emit(NetworkResult.Error("nothing to update"))
                    }
                } else {
                    emit(NetworkResult.Error("id is null"))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "something went wrong"))
            }
        }
    }

    override suspend fun createOrder(order: OrderModel): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    val orderId = UUID.randomUUID().toString()
                    val items = itemDao.getSelectedItems()
                    order.id = orderId
                    order.price = items.sumOf { it.price ?: 0 }
                    order.items = items.associate { it.name.toString() to 1 }
                    order.storeReference = firebaseFirestore
                        .collection(STORES)
                        .document(currentStore.uid)

                    firebaseFirestore.collection(ORDERS)
                        .document(orderId)
                        .set(order)
                        .await()

                    emit(NetworkResult.Success(true))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "can't create order"))
            }
        }
    }

    override fun getOrdersCallback(): Flow<List<OrderModel>> {
        return callbackFlow {
            val currentStore = firebaseAuth.currentUser
            if (currentStore != null) {
                val storeReference = firebaseFirestore.collection(STORES).document(currentStore.uid)
                val listener = firebaseFirestore.collection(ORDERS)
                    .whereEqualTo(STORE_REFERENCE, storeReference)
                    .whereGreaterThanOrEqualTo(CREATE_ORDER_DATE, DateFilter.getStartOfDay())
                    .whereLessThan(CREATE_ORDER_DATE, DateFilter.getStartOfNextDay())
                    .orderBy(CREATE_ORDER_DATE, Query.Direction.DESCENDING)
                    .addSnapshotListener { value, error ->
                        if (error != null) close(error)
                        if (value != null) {
                            trySend(value.toObjects<OrderModel>())
                        }
                    }
                awaitClose { listener.remove() }
            }
        }
    }

    override suspend fun getOrders(): Flow<NetworkResult<List<OrderModel>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    val storeReference =
                        firebaseFirestore.collection(STORES).document(currentStore.uid)
                    val snapshot = firebaseFirestore.collection(ORDERS)
                        .whereEqualTo(STORE_REFERENCE, storeReference)
                        .whereGreaterThanOrEqualTo(CREATE_ORDER_DATE, DateFilter.getStartOfDay())
                        .whereLessThan(CREATE_ORDER_DATE, DateFilter.getStartOfNextDay())
                        .get()
                        .await()
                        .documents

                    val orders = snapshot.mapNotNull { it.toObject<OrderModel>() }
                    if (orders.isNotEmpty()) {
                        emit(NetworkResult.Success(orders))
                    } else {
                        emit(NetworkResult.Error(message = "no orders"))
                    }
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.localizedMessage ?: "can't get orders"))
            }
        }
    }

    override suspend fun getItemById(id: String): Flow<ItemModel?> {
        return flow {
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    val snapshot = firebaseFirestore.collection(ITEMS)
                        .document(id)
                        .get()
                        .await()

                    emit(snapshot.toObject<ItemModel>())
                }
            } catch (e: Exception) {
                emit(null)
            }
        }
    }

    override suspend fun cancelOrder(id: String): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    firebaseFirestore.collection(ORDERS)
                        .document(id)
                        .update(ORDER_STATUS, DECLINED)
                        .await()
                    emit(NetworkResult.Success(true))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "can't decline order"))
            }
        }
    }

    override suspend fun placeOrder(id: String): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    firebaseFirestore.collection(ORDERS)
                        .document(id)
                        .update(ORDER_STATUS, READY)
                        .await()

                    firebaseFirestore.collection(ORDERS)
                        .document(id)
                        .update(READY_ORDER_DATE, FieldValue.serverTimestamp())
                        .await()

                    emit(NetworkResult.Success(true))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(message = e.localizedMessage ?: "can't place order"))
            }
        }
    }

    override suspend fun getTotalOrdersInRange(start: Timestamp, end: Timestamp): Flow<Long> {
        return flow {
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    val storeReference =
                        firebaseFirestore.collection(STORES).document(currentStore.uid)
                    val query = firebaseFirestore.collection(ORDERS)
                        .whereEqualTo(STORE_REFERENCE, storeReference)
                        .whereGreaterThanOrEqualTo(CREATE_ORDER_DATE, start)
                        .whereLessThan(CREATE_ORDER_DATE, end)
                        .count()
                        .get(AggregateSource.SERVER)
                        .await()

                    emit(query.count)
                }
            } catch (e: Exception) {
                emit(-1)
            }
        }
    }

    override suspend fun getTotalRevenueInRange(start: Timestamp, end: Timestamp): Flow<Int> {
        return flow {
            try {
                val currentStore = firebaseAuth.currentUser
                if (currentStore != null) {
                    val storeReference = firebaseFirestore.collection(STORES).document(currentStore.uid)
                    val snapshot = firebaseFirestore.collection(ORDERS)
                        .whereEqualTo(STORE_REFERENCE, storeReference)
                        .whereGreaterThanOrEqualTo(CREATE_ORDER_DATE, start)
                        .whereLessThan(CREATE_ORDER_DATE, end)
                        .get()
                        .await()
                        .documents

                    val revenue = snapshot.sumOf { it.toObject<OrderModel>()?.price ?: 0 }
                    emit(revenue)
                }
            } catch (e: Exception) {
                emit(-1)
            }
        }
    }

    companion object Constants {
        const val STORES = "stores"
        const val ITEMS = "items"
        const val STORE_REFERENCE = "storeReference"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val CATEGORY = "category"
        const val PRICE = "price"
        const val IMAGE_URL = "imageUrl"
        const val TYPE = "type"
        const val ADDRESS = "address"
        const val PROFILE_PICTURE_URL = "profilePictureUrl"
        const val ORDERS = "orders"
        const val ID = "id"
        const val ORDER_STATUS = "orderStatus"
        const val READY = "Готов"
        const val DECLINED = "Отменен"
        const val READY_ORDER_DATE = "readyOrderDate"
        const val CREATE_ORDER_DATE = "createOrderDate"
    }
}