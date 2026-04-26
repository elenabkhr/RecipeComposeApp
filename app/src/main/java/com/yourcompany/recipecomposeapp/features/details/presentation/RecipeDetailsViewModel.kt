package com.yourcompany.recipecomposeapp.features.details.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yourcompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.yourcompany.recipecomposeapp.data.Constants
import com.yourcompany.recipecomposeapp.data.repository.RecipeRepositoryStub.getRecipeById
import com.yourcompany.recipecomposeapp.features.details.presentation.model.IngredientUiModel
import com.yourcompany.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.yourcompany.recipecomposeapp.features.details.presentation.model.toUiModel
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val favoriteManager = FavoriteDataStoreManager(application)
    private val recipeId = savedStateHandle.get<Int>(Constants.KEY_RECIPE_ID)
        ?: throw IllegalArgumentException("recipeId is required")

    private val _uiState = MutableStateFlow(RecipeDetailsUiState())
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    init {
        loadRecipe(recipeId)
    }

    fun loadRecipe(recipeId: Int) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true, isError = null)
        }

        try {
            val recipe = getRecipeById(recipeId)
            _uiState.update { currentState ->
                currentState.copy(
                    recipe = recipe?.toUiModel(),
                    scaledIngredients = recipe?.ingredients?.map { it.toUiModel() } ?: emptyList(),
                    isLoading = false,
                )
            }
            observeFavorite()
        } catch (e: Exception) {
            _uiState.update { currentState ->
                currentState.copy(isLoading = false, isError = e.message)
            }
        }
    }

    private fun observeFavorite() {
        viewModelScope.launch {
            _uiState.value.recipe?.let { favoriteManager.isFavoriteFlow(it.id) }
                ?.collect { isFavorite ->
                    _uiState.update { it.copy(isFavorite = isFavorite) }
                }
        }
    }

    fun toggleFavorite() {
        val recipeId = _uiState.value.recipe?.id ?: return

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
