package com.swmpire.delifyit.presentation.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.usecase.GetFirebaseStoreSignStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private  val getFirebaseStoreSignStatusUseCase: GetFirebaseStoreSignStatusUseCase
) : ViewModel() {

    private val _launchMainScreen: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val launchMainScreen: LiveData<Boolean> get() = _launchMainScreen

    init {
        viewModelScope.launch {
            _launchMainScreen.value = getFirebaseStoreSignStatusUseCase.invoke()
        }
    }
}