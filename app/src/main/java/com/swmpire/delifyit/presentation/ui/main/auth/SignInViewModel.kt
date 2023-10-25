package com.swmpire.delifyit.presentation.ui.main.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.SignInStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInStoreUseCase: SignInStoreUseCase
) : ViewModel() {

    private val _signInFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    val signInFlow: StateFlow<NetworkResult<Boolean>> get() = _signInFlow

    fun signInStore(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signInStoreUseCase.invoke(email = email, password =  password).collect() { result ->
                _signInFlow.value = result
            }
        }
    }
}