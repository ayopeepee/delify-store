package com.swmpire.delifyit.presentation.ui.main.tabs.profile

import androidx.lifecycle.ViewModel
import com.swmpire.delifyit.domain.usecase.SignOutStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutStoreUseCase: SignOutStoreUseCase
) : ViewModel() {

    fun signOut() {
        signOutStoreUseCase.invoke()
    }
}