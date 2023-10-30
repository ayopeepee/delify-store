package com.swmpire.delifyit.presentation.ui.main.tabs.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.GetAllItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val getAllItemsUseCase: GetAllItemsUseCase
) : ViewModel() {
    private val _itemsFlow = MutableStateFlow<NetworkResult<List<ItemModel>>>(NetworkResult.Idle())
    val itemsFlow: StateFlow<NetworkResult<List<ItemModel>>> get() = _itemsFlow

    init {
        getAllItems()
    }
    fun getAllItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllItemsUseCase.invoke().collect() { result->
                _itemsFlow.value = result
            }
        }
    }
}