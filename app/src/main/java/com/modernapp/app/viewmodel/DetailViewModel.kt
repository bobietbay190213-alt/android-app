package com.modernapp.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modernapp.app.domain.model.Item
import com.modernapp.app.domain.usecase.AddItemUseCase
import com.modernapp.app.domain.usecase.DeleteItemUseCase
import com.modernapp.app.domain.usecase.GetItemsUseCase
import com.modernapp.app.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val item: Item? = null,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val editTitle: String = "",
    val editBody: String = ""
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase,
    private val addItemUseCase: AddItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val itemId: Long = savedStateHandle.get<Long>("itemId") ?: -1L

    init {
        loadItem()
    }

    private fun loadItem() {
        if (itemId <= 0L) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getItemsUseCase.getItemById(itemId)) {
                is Result.Success -> _uiState.update { state ->
                    state.copy(
                        item = result.data,
                        isLoading = false,
                        editTitle = result.data.title,
                        editBody = result.data.body
                    )
                }
                is Result.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
            }
        }
    }

    fun startEditing() = _uiState.update { it.copy(isEditing = true) }

    fun cancelEditing() = _uiState.update { state ->
        state.copy(
            isEditing = false,
            editTitle = state.item?.title ?: "",
            editBody = state.item?.body ?: ""
        )
    }

    fun onTitleChange(title: String) = _uiState.update { it.copy(editTitle = title) }
    fun onBodyChange(body: String) = _uiState.update { it.copy(editBody = body) }

    fun saveChanges() {
        val currentItem = _uiState.value.item ?: return
        viewModelScope.launch {
            val updatedItem = currentItem.copy(
                title = _uiState.value.editTitle.trim(),
                body = _uiState.value.editBody.trim()
            )
            when (val result = addItemUseCase.update(updatedItem)) {
                is Result.Success -> _uiState.update { it.copy(item = updatedItem, isEditing = false, isSaved = true) }
                is Result.Error -> _uiState.update { it.copy(error = result.message) }
                is Result.Loading -> {}
            }
        }
    }

    fun toggleFavorite() {
        val item = _uiState.value.item ?: return
        viewModelScope.launch {
            deleteItemUseCase.toggleFavorite(item.id, !item.isFavorite)
            _uiState.update { it.copy(item = item.copy(isFavorite = !item.isFavorite)) }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
    fun clearSaved() = _uiState.update { it.copy(isSaved = false) }
}
