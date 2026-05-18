package com.yourcompany.recipecomposeapp.features.categories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepository
import com.yourcompany.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import com.yourcompany.recipecomposeapp.features.categories.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository: RecipesRepository) : ViewModel() {
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

            try {
                repository.getCategories()
                    .map { dtos -> dtos.map { it.toUiModel() } }
                    .collect { categories ->
                        _uiState.update { it.copy(categories = categories, isLoading = false) }
                    }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, isError = e.message)
                }
            }
        }
    }
}
