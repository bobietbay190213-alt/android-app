package com.modernapp.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modernapp.app.domain.model.Item
import com.modernapp.app.domain.usecase.AddItemUseCase
import com.modernapp.app.domain.usecase.DeleteItemUseCase
import com.modernapp.app.domain.usecase.GetItemsUseCase
import com.modernapp.app.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val showAddDialog: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase,
    private val addItemUseCase: AddItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val items: StateFlow<List<Item>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            getItemsUseCase.searchItems(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = getItemsUseCase.fetchFromApi()
            _uiState.update { state ->
                when (result) {
                    is Result.Success -> state.copy(isLoading = false, error = null)
                    is Result.Error -> state.copy(isLoading = false, error = result.message)
                    is Result.Loading -> state.copy(isLoading = true)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val result = getItemsUseCase.fetchFromApi()
            _uiState.update { state ->
                when (result) {
                    is Result.Success -> state.copy(isRefreshing = false, error = null)
                    is Result.Error -> state.copy(isRefreshing = false, error = result.message)
                    is Result.Loading -> state.copy(isRefreshing = true)
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun addItem(title: String, body: String) {
        viewModelScope.launch {
            val result = addItemUseCase(title, body)
            _uiState.update { state ->
                when (result) {
                    is Result.Success -> state.copy(showAddDialog = false, error = null)
                    is Result.Error -> state.copy(error = result.message)
                    is Result.Loading -> state
                }
            }
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            deleteItemUseCase(id)
        }
    }

    fun toggleFavorite(id: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            deleteItemUseCase.toggleFavorite(id, !isFavorite)
        }
    }

    fun showAddDialog() = _uiState.update { it.copy(showAddDialog = true) }
    fun hideAddDialog() = _uiState.update { it.copy(showAddDialog = false) }
    fun clearError() = _uiState.update { it.copy(error = null) }
}
