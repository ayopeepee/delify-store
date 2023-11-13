package com.swmpire.delifyit.presentation.ui.main.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.AddProfilePictureUseCase
import com.swmpire.delifyit.domain.usecase.CreateFirestoreStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetStoreInfoViewModel @Inject constructor(
    private val createFirestoreStoreUseCase: CreateFirestoreStoreUseCase,
    private val addProfilePictureUseCase: AddProfilePictureUseCase
) : ViewModel() {
    private val _createStoreFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    private val _uploadProfilePictureFlow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Idle())
    val createStoreFlow: StateFlow<NetworkResult<Boolean>> get() = _createStoreFlow
    val uploadProfilePictureFlow: StateFlow<NetworkResult<String>> get() = _uploadProfilePictureFlow.asStateFlow()

    fun createStore(name: String, description: String, address: String, type: String, profilePictureUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            createFirestoreStoreUseCase.invoke(name, description, address, type, profilePictureUrl).collect() { result ->
                _createStoreFlow.value = result
            }
        }
    }
    fun uploadProfilePicture(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            addProfilePictureUseCase.invoke(uri).collect { url ->
                _uploadProfilePictureFlow.value = url
            }
        }
    }
}