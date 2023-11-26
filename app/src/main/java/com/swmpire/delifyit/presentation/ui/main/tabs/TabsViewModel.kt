package com.swmpire.delifyit.presentation.ui.main.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swmpire.delifyit.domain.usecase.GetNumberOfOrdersToProceedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    private val getNumberOfOrdersToProceedUseCase: GetNumberOfOrdersToProceedUseCase
) : ViewModel() {
    private val _numberOfOrdersToProceedFlow = MutableStateFlow<Int>(0)
    val numberOfOrdersToProceedFlow get() = _numberOfOrdersToProceedFlow.asStateFlow()

    init {
        getNumberOfOrdersToProceed()
    }
    fun getNumberOfOrdersToProceed() {
        viewModelScope.launch(Dispatchers.IO) {
            getNumberOfOrdersToProceedUseCase.invoke().collect { result ->
                _numberOfOrdersToProceedFlow.value = result
            }
        }
    }
}