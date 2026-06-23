package com.modernapp.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modernapp.app.domain.usecase.DeleteItemUseCase
import com.modernapp.app.utils.Constants
import com.modernapp.app.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: String = Constants.THEME_SYSTEM,
    val language: String = "vi",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val deleteItemUseCase: DeleteItemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setThemeMode(mode: String) {
        _uiState.update { it.copy(themeMode = mode) }
    }

    fun setLanguage(language: String) {
        _uiState.update { it.copy(language = language) }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = deleteItemUseCase.deleteAll()) {
                is Result.Success -> _uiState.update {
                    it.copy(isLoading = false, successMessage = "All data deleted successfully")
                }
                is Result.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun clearMessages() = _uiState.update { it.copy(successMessage = null, error = null) }
}
