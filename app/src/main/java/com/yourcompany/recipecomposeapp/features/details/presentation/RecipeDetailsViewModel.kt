package com.yourcompany.recipecomposeapp.features.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.yourcompany.recipecomposeapp.data.Constants
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepository
import com.yourcompany.recipecomposeapp.features.details.presentation.model.IngredientUiModel
import com.yourcompany.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val favoriteManager: FavoriteDataStoreManager,
    savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository,
) : ViewModel() {
    private val recipeId = savedStateHandle.get<Int>(Constants.KEY_RECIPE_ID)
        ?: throw IllegalArgumentException("recipeId is required")

    private val _uiState = MutableStateFlow(RecipeDetailsUiState())
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    init {
        loadRecipe(recipeId)
        observeFavorite()
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, isError = null)
            }

            try {
                repository.getRecipe(recipeId)
                    .map { dto -> dto?.toUiModel() }
                    .collect { recipe ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                recipe = recipe,
                                scaledIngredients = recipe?.ingredients ?: emptyList(),
                                isLoading = recipe == null,
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

    private fun observeFavorite() {
        viewModelScope.launch {
            favoriteManager.isFavoriteFlow(recipeId)
                .collect { isFavorite ->
                    _uiState.update { it.copy(isFavorite = isFavorite) }
                }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                favoriteManager.removeFavorite(recipeId)
            } else {
                favoriteManager.addFavorite(recipeId)
            }
        }
    }

    fun updatePortions(sliderValue: Int) {
        val newPortions = sliderValue.coerceAtLeast(1)
        _uiState.update {
            it.copy(
                currentPortions = newPortions,
                scaledIngredients = recalculateIngredients(newPortions)
            )
        }
    }

    private fun recalculateIngredients(newPortions: Int): List<IngredientUiModel> {
        val recipe = _uiState.value.recipe ?: return emptyList()
        val multiplier = newPortions.toDouble() / recipe.servings
        return recipe.ingredients.map { ingredient ->
            ingredient.copy(quantity = (ingredient.quantity * multiplier))
        }
    }
}
