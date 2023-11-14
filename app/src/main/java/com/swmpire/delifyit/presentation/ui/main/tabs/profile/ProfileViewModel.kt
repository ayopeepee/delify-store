package com.swmpire.delifyit.presentation.ui.main.tabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.usecase.GetStoreUseCase
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
    private val getStoreUseCase: GetStoreUseCase
) : ViewModel() {

    private val _getStoreFlow = MutableStateFlow<NetworkResult<StoreModel>>(NetworkResult.Idle())
    val getStoreFlow: StateFlow<NetworkResult<StoreModel>> get() = _getStoreFlow.asStateFlow()

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
}