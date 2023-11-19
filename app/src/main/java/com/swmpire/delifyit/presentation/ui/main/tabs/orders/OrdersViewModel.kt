package com.swmpire.delifyit.presentation.ui.main.tabs.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.OrderModel
import com.swmpire.delifyit.domain.usecase.CancelOrderUseCase
import com.swmpire.delifyit.domain.usecase.GetCallbackOrdersUseCase
import com.swmpire.delifyit.domain.usecase.GetItemByIdUseCase
import com.swmpire.delifyit.domain.usecase.GetOrdersUseCase
import com.swmpire.delifyit.domain.usecase.PlaceOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val getCallbackOrdersUseCase: GetCallbackOrdersUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase
) : ViewModel() {
    private val _getOrdersFlow = MutableStateFlow<NetworkResult<List<OrderModel>>>(NetworkResult.Idle())
    private val _getOrdersCallbackFlow = MutableStateFlow<List<OrderModel>>(emptyList())
    private val _placeOrderFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    private val _cancelOrderFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())


    val getOrdersFlow: StateFlow<NetworkResult<List<OrderModel>>> get() = _getOrdersFlow.asStateFlow()
    val getOrdersCallbackFlow: StateFlow<List<OrderModel>> get() = _getOrdersCallbackFlow.asStateFlow()
    val placeOrderFlow: StateFlow<NetworkResult<Boolean>> get() = _placeOrderFlow.asStateFlow()
    val cancelOrderFlow: StateFlow<NetworkResult<Boolean>> get() = _cancelOrderFlow.asStateFlow()


    init {
        getOrders()
        getCallbackOrders()
    }
    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            getOrdersUseCase.invoke().collect { result ->
                _getOrdersFlow.value = result
            }
        }
    }
    fun getCallbackOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            getCallbackOrdersUseCase.invoke().collect { result ->
                _getOrdersCallbackFlow.value = result
            }
        }
    }
    fun placeOrder(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            placeOrderUseCase.invoke(id).collect { result ->
                _placeOrderFlow.value = result
            }
        }
    }
    fun cancelOrder(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelOrderUseCase.invoke(id).collect { result ->
                _cancelOrderFlow.value = result
            }
        }
    }

}