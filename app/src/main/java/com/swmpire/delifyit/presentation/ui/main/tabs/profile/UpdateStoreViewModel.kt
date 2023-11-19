package com.swmpire.delifyit.presentation.ui.main.tabs.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.uitl.TimeIntervals
import com.swmpire.delifyit.domain.usecase.AddProfilePictureUseCase
import com.swmpire.delifyit.domain.usecase.GetStoreUseCase
import com.swmpire.delifyit.domain.usecase.GetTotalOrdersUseCase
import com.swmpire.delifyit.domain.usecase.UpdateStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateStoreViewModel @Inject constructor(
    private val updateStoreUseCase: UpdateStoreUseCase,
    private val addProfilePictureUseCase: AddProfilePictureUseCase,
    private val getStoreUseCase: GetStoreUseCase
) : ViewModel() {

    private val _updateStoreFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    private val _uploadProfilePictureFLow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Idle())
    private val _getStoreFlow = MutableStateFlow<NetworkResult<StoreModel>>(NetworkResult.Idle())
    val updateStoreFlow: StateFlow<NetworkResult<Boolean>> get() = _updateStoreFlow.asStateFlow()
    val uploadProfilePictureFlow: StateFlow<NetworkResult<String>> get() = _uploadProfilePictureFLow.asStateFlow()
    val getStoreFlow: StateFlow<NetworkResult<StoreModel>> get() = _getStoreFlow.asStateFlow()

    init {
        getStore()
    }
    fun updateStore(store: StoreModel) {
        viewModelScope.launch(Dispatchers.IO) {
            updateStoreUseCase.invoke(store).collect { result ->
                _updateStoreFlow.value = result
            }
        }
    }

    fun uploadProfilePicture(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            addProfilePictureUseCase.invoke(uri).collect { url ->
                _uploadProfilePictureFLow.value = url
            }
        }
    }

    fun getStore() {
        viewModelScope.launch(Dispatchers.IO) {
            getStoreUseCase.invoke().collect { result ->
                _getStoreFlow.value = result
            }
        }
    }
}