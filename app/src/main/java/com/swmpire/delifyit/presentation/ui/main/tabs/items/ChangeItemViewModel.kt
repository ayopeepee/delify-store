package com.swmpire.delifyit.presentation.ui.main.tabs.items

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.ItemModel
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.AddItemImageUseCase
import com.swmpire.delifyit.domain.usecase.UpdateItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeItemViewModel @Inject constructor(
    private val updateItemUseCase: UpdateItemUseCase,
    private val addItemImageUseCase: AddItemImageUseCase
) : ViewModel() {
    private val _updateItemFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    private val _addItemImageFlow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Idle())
    val updateItemFlow: StateFlow<NetworkResult<Boolean>> get() = _updateItemFlow.asStateFlow()
    val addItemImageFlow: StateFlow<NetworkResult<String>> get() = _addItemImageFlow.asStateFlow()

    fun updateItem(item: ItemModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateItemUseCase.invoke(item).collect { result ->
                _updateItemFlow.value = result
            }
        }
    }

    fun addItemImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            addItemImageUseCase.invoke(uri).collect() { result ->
                _addItemImageFlow.value = result
            }
        }
    }
}