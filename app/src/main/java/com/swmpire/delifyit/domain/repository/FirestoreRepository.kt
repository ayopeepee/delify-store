package com.swmpire.delifyit.domain.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.OrderModel
import com.swmpire.delifyit.domain.model.StoreModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend fun createStore(store: StoreModel) : Flow<NetworkResult<Boolean>>
    suspend fun addItem(item: ItemModel) : Flow<NetworkResult<Boolean>>
    suspend fun getAllItems() : Flow<NetworkResult<List<ItemModel>>>
    fun getItemsCallback() : Flow<List<ItemModel>>
    suspend fun updateItem(item: ItemModel) : Flow<NetworkResult<Boolean>>
    suspend fun deleteSelectedItems() : Flow<NetworkResult<Boolean>>
    suspend fun getStore() : Flow<NetworkResult<StoreModel>>
    suspend fun updateStore(store: StoreModel) : Flow<NetworkResult<Boolean>>
    suspend fun createOrder(order: OrderModel) : Flow<NetworkResult<Boolean>>
    fun getOrdersCallback() : Flow<List<OrderModel>>
    suspend fun getOrders() : Flow<NetworkResult<List<OrderModel>>>
    fun getNumberOfOrdersToProceedCallback() : Flow<Int>
    suspend fun getItemById(id: String) : Flow<ItemModel?>
    suspend fun cancelOrder(id: String) : Flow<NetworkResult<Boolean>>
    suspend fun placeOrder(id: String) : Flow<NetworkResult<Boolean>>
    suspend fun getTotalOrdersInRange(start: Timestamp, end: Timestamp) : Flow<Long>
    suspend fun getTotalRevenueInRange(start: Timestamp, end: Timestamp) : Flow<Int>
}