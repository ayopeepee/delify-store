package com.swmpire.delifyit.presentation.ui.main.tabs.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.GetAllItemsUseCase
import com.swmpire.delifyit.domain.usecase.GetCallbackItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val getAllItemsUseCase: GetAllItemsUseCase,
    private val getCallbackItemsUseCase: GetCallbackItemsUseCase
) : ViewModel() {
    private val _itemsFlow = MutableStateFlow<NetworkResult<List<ItemModel>>>(NetworkResult.Idle())
    private val _itemsCallbackFlow = MutableStateFlow<List<ItemModel>>(emptyList())
    val itemsFlow: StateFlow<NetworkResult<List<ItemModel>>> get() = _itemsFlow
    val itemsCallbackFlow: StateFlow<List<ItemModel>> get() = _itemsCallbackFlow.asStateFlow()

    init {
        getAllItems()
        getCallbackItems()
    }
    fun getAllItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllItemsUseCase.invoke().collect() { result->
                _itemsFlow.value = result
            }
        }
    }
    fun getCallbackItems() {
        viewModelScope.launch {
            getCallbackItemsUseCase.invoke().collect { result ->
                _itemsCallbackFlow.value = result
            }
        }
    }
}