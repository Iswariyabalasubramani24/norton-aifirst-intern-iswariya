package com.iswariya.scammessagedetector.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iswariya.scammessagedetector.domain.usecase.AnalyzeInputUseCase
import com.iswariya.scammessagedetector.presentation.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val analyzeInput: AnalyzeInputUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onInputChanged(text: String) {
        _uiState.update { it.copy(inputText = text, result = null, error = null) }
    }

    fun onAnalyze() {
        val input = _uiState.value.inputText.trim()
        if (input.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, result = null, error = null) }
            analyzeInput(input).fold(
                onSuccess = { result ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            result = result
                        )
                    }
                },
                onFailure = { e ->
                    val message = when (e) {
                        is HttpException -> "Service error (${e.code()}). Please try again later."
                        is IOException -> "Network error. Check your connection and try again."
                        else -> "Analysis failed. Please try again."
                    }
                    _uiState.update { it.copy(isLoading = false, error = message) }
                }
            )
        }
    }
}
