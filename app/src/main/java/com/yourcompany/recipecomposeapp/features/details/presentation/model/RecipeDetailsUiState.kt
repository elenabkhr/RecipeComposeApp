package com.yourcompany.recipecomposeapp.features.details.presentation.model

import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

data class RecipeDetailsUiState(
    val recipe: RecipeUiModel? = null,
    val currentPortions: Int = 1,
    val scaledIngredients: List<IngredientUiModel> = emptyList(),
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val isError: String? = null,
)
