package com.yourcompany.recipecomposeapp.features.details.presentation.model

import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

data class RecipeDetailsUiState(
    val recipe: RecipeUiModel? = null,
    val isFavorite: Boolean = false,
    val portions: Int = 1,
    val isLoading: Boolean = false,
) {
    val scaledIngredients: List<IngredientUiModel>
        get() {
            val recipe = recipe ?: return emptyList()
            val multiplier = portions.toDouble() / recipe.servings

            return recipe.ingredients.map { ingredient ->
                ingredient.copy(quantity = (ingredient.quantity * multiplier))
            }
        }
}
