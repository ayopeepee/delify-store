package com.swmpire.delifyit.presentation.ui.main.tabs.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.DeleteSelectedItemsUseCase
import com.swmpire.delifyit.domain.usecase.DeselectAllItemsUseCase
import com.swmpire.delifyit.domain.usecase.GetAllItemsUseCase
import com.swmpire.delifyit.domain.usecase.GetCallbackItemsUseCase
import com.swmpire.delifyit.domain.usecase.GetSelectedItemsCountUseCase
import com.swmpire.delifyit.domain.usecase.UpdateSelectStatusUseCase
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
    private val getCallbackItemsUseCase: GetCallbackItemsUseCase,
    private val updateSelectStatusUseCase: UpdateSelectStatusUseCase,
    private val getSelectedItemsCountUseCase: GetSelectedItemsCountUseCase,
    private val deselectAllItemsUseCase: DeselectAllItemsUseCase,
    private val deleteSelectedItemsUseCase: DeleteSelectedItemsUseCase
) : ViewModel() {
    private val _itemsFlow = MutableStateFlow<NetworkResult<List<ItemModel>>>(NetworkResult.Idle())
    private val _itemsCallbackFlow = MutableStateFlow<List<ItemModel>>(emptyList())
    private val _selectedItemsCount = MutableLiveData<Int>()
    private val _deleteSelectedItems = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    val itemsFlow: StateFlow<NetworkResult<List<ItemModel>>> get() = _itemsFlow
    val itemsCallbackFlow: StateFlow<List<ItemModel>> get() = _itemsCallbackFlow.asStateFlow()
    val selectedItemsCount: LiveData<Int> get() = _selectedItemsCount
    val deleteSelectedItems: StateFlow<NetworkResult<Boolean>> get() = _deleteSelectedItems

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
    fun getCallbackItems() {
        viewModelScope.launch {
            getCallbackItemsUseCase.invoke().collect { result ->
                _itemsCallbackFlow.value = result
            }
        }
    }
    fun setIsSelected(id: String?, isSelected: Boolean) {
        viewModelScope.launch {
            updateSelectStatusUseCase.invoke(id, isSelected)
            _selectedItemsCount.value = getSelectedItemsCountUseCase.invoke()
        }
    }
    fun deleteSelectedItems() {
        viewModelScope.launch {
            deleteSelectedItemsUseCase.invoke().collect { result ->
                _deleteSelectedItems.value = result
            }
        }
    }
}