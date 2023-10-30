package com.swmpire.delifyit.presentation.ui.main.tabs.items

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.AddItemImageUseCase
import com.swmpire.delifyit.domain.usecase.AddItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val addItemUseCase: AddItemUseCase,
    private val addItemImageUseCase: AddItemImageUseCase
) : ViewModel() {
    private val _addItemFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    private val _addItemImageFlow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Idle())
    val addItemFlow: StateFlow<NetworkResult<Boolean>> get() = _addItemFlow
    val addItemImageFlow: StateFlow<NetworkResult<String>> get() = _addItemImageFlow

    fun addItem(name: String, description: String, category: String, price: Int, url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addItemUseCase.invoke(
                name,
                description,
                category,
                price,
                url
            ).collect() { result ->
                _addItemFlow.value = result
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