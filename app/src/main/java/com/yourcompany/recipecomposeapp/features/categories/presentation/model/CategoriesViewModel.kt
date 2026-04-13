package com.yourcompany.recipecomposeapp.features.categories.presentation.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.recipecomposeapp.data.repository.RecipeRepositoryStub
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, isError = null)
            }
        }

        try {
            val categories = RecipeRepositoryStub
                .getCategories()
                .map { it.toUiModel() }

            _uiState.update { currentState ->
                currentState.copy(categories = categories, isLoading = false)
            }
        } catch (e: Exception) {
            _uiState.update { currentState ->
                currentState.copy(isLoading = false, isError = e.message)
            }
        }
    }
}