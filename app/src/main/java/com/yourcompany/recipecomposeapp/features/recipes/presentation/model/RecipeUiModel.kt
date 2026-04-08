package com.yourcompany.recipecomposeapp.features.recipes.presentation.model

import androidx.compose.runtime.Immutable
import com.yourcompany.recipecomposeapp.data.ASSETS_URI_PREFIX
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import com.yourcompany.recipecomposeapp.features.details.presentation.model.IngredientUiModel
import com.yourcompany.recipecomposeapp.features.details.presentation.model.toUiModel
import kotlin.Int

@Immutable
data class RecipeUiModel(
    val id: Int,
    val title: String,
    val ingredients: List<IngredientUiModel>,
    val method: List<String>,
    val imageUrl: String,
    val isFavorite: Boolean,
    val servings: Int = 1,
)

fun RecipeDto.toUiModel() = RecipeUiModel(
    id = id,
    title = title,
    ingredients = ingredients.map { it.toUiModel() },
    method = method,
    imageUrl = if (imageUrl.startsWith("http")) imageUrl else ASSETS_URI_PREFIX + imageUrl,
    isFavorite = false
)

