package com.swmpire.delifyit.presentation.ui.main.tabs.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.AddItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val addItemUseCase: AddItemUseCase
) : ViewModel() {
    private val _addItemFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    val addItemFlow: StateFlow<NetworkResult<Boolean>> get() = _addItemFlow

    fun addItem(name: String, description: String, category: String, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            addItemUseCase.invoke(name, description, category, price).collect() { result ->
                _addItemFlow.value = result
            }
        }
    }
}