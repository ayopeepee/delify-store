package com.swmpire.delifyit.presentation.ui.main.tabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.uitl.TimeIntervals
import com.swmpire.delifyit.domain.usecase.GetStoreUseCase
import com.swmpire.delifyit.domain.usecase.GetTotalOrdersUseCase
import com.swmpire.delifyit.domain.usecase.GetTotalRevenueUseCase
import com.swmpire.delifyit.domain.usecase.SignOutStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutStoreUseCase: SignOutStoreUseCase,
    private val getStoreUseCase: GetStoreUseCase,
    private val getTotalOrdersUseCase: GetTotalOrdersUseCase,
    private val getTotalRevenueUseCase: GetTotalRevenueUseCase
) : ViewModel() {

    private val _getStoreFlow = MutableStateFlow<NetworkResult<StoreModel>>(NetworkResult.Idle())
    private val _getTotalOrdersFlow = MutableStateFlow<Long>(0)
    private val _getTotalRevenueFlow = MutableStateFlow<Int>(0)
    val getStoreFlow: StateFlow<NetworkResult<StoreModel>> get() = _getStoreFlow.asStateFlow()
    val getTotalOrdersFlow: StateFlow<Long> get() = _getTotalOrdersFlow.asStateFlow()
    val getTotalRevenueFlow: StateFlow<Int> get() = _getTotalRevenueFlow.asStateFlow()

    init {
        getStore()
    }

    fun getStore() {
        viewModelScope.launch(Dispatchers.IO) {
            getStoreUseCase.invoke().collect { store ->
                _getStoreFlow.value = store
            }
        }
    }
    fun signOut() {
        signOutStoreUseCase.invoke()
    }

    fun getTotalOrdersByTimeInterval(interval: TimeIntervals) {
        viewModelScope.launch(Dispatchers.IO) {
            getTotalOrdersUseCase.invoke(interval).collect { result ->
                _getTotalOrdersFlow.value = result
            }
        }
    }
    fun getTotalRevenueByTimeInterval(interval: TimeIntervals) {
        viewModelScope.launch(Dispatchers.IO) {
            getTotalRevenueUseCase.invoke(interval).collect { result ->
                _getTotalRevenueFlow.value = result
            }
        }
    }
    fun getCurrentOrdersAndRevenue() {
        getTotalOrdersByTimeInterval(TimeIntervals.DAY)
        getTotalRevenueByTimeInterval(TimeIntervals.DAY)
    }
}