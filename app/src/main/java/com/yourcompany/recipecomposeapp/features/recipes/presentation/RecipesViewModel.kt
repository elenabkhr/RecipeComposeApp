package com.yourcompany.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.recipecomposeapp.data.Constants
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepository
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder

class RecipesViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipesUiState())
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    private val categoryId = savedStateHandle.get<Int>(Constants.KEY_CATEGORY_ID)
        ?: throw IllegalArgumentException("categoryId is required")

    private val categoryTitle =
        savedStateHandle.get<String>(Constants.KEY_CATEGORY_TITLE).let { title ->
            URLDecoder.decode(title ?: "", "UTF-8")
        }
    private val categoryImageUrl =
        savedStateHandle.get<String>(Constants.KEY_CATEGORY_IMAGE_URL).let { imageUrl ->
            URLDecoder.decode(imageUrl ?: "", "UTF-8")
        }

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, isError = null)
            }

            try {
                repository.getRecipesByCategories(categoryId)
                    .map { dtos -> dtos.map { it.toUiModel() } }
                    .collect { recipes ->
                        _uiState.update {
                            it.copy(
                                recipes = recipes,
                                categoryTitle = categoryTitle,
                                categoryImageUrl = categoryImageUrl,
                                isLoading = false
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, isError = e.message)
                }
            }
        }
    }
}
