package com.swmpire.delifyit.presentation.ui.main.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.CreateFirestoreStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetStoreInfoViewModel @Inject constructor(
    private val createFirestoreStoreUseCase: CreateFirestoreStoreUseCase
) : ViewModel() {
    private val _createStoreFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    val createStoreFlow: StateFlow<NetworkResult<Boolean>> get() = _createStoreFlow

    fun createStore(name: String, description: String, address: String, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            createFirestoreStoreUseCase.invoke(name, description, address, type).collect() { result ->
                _createStoreFlow.value = result
            }
        }
    }
}