package com.yourcompany.recipecomposeapp.features.favorites.presentation

import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

data class FavoritesUiState(
    val recipes: List<RecipeUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
    val isError: String? = null
)
