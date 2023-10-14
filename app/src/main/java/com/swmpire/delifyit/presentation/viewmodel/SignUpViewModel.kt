package com.swmpire.delifyit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.model.StoreModel
import com.swmpire.delifyit.domain.usecase.GetFirebaseStoreUseCase
import com.swmpire.delifyit.domain.usecase.SignUpStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpStoreUseCase: SignUpStoreUseCase,
    private val getFirebaseStoreUseCase: GetFirebaseStoreUseCase
) : ViewModel() {

    private val _signUpFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    val signUpFlow: StateFlow<NetworkResult<Boolean>> get() = _signUpFlow

    private val currentStore: FirebaseUser? get() = getFirebaseStoreUseCase.invoke()

    // remove init to disable auto-auth
    init {
        currentStore.let {
            _signUpFlow.value = NetworkResult.Success(true)
        }
    }

    fun signUpStore(storeModel: StoreModel) {
        viewModelScope.launch(Dispatchers.IO) {
            signUpStoreUseCase.invoke(storeModel = storeModel).collect() { result ->
                _signUpFlow.value = result
            }
        }
    }
}
