package com.swmpire.delifyit.presentation.ui.main.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.model.NetworkResult
import com.swmpire.delifyit.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    private val _resetPasswordFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
    val resetPasswordFlow: StateFlow<NetworkResult<Boolean>> get() = _resetPasswordFlow

    fun resetPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            resetPasswordUseCase.invoke(email = email).collect() { result ->
                _resetPasswordFlow.value = result
            }
        }
    }
}