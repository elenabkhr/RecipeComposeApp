package com.yourcompany.recipecomposeapp.ui.recipes.model

import androidx.compose.runtime.Immutable
import com.yourcompany.recipecomposeapp.data.model.IngredientDto

@Immutable
data class IngredientUiModel(
    val name: String,
    val amount: String,
)

fun IngredientDto.toUiModel() = IngredientUiModel(
    name = description,
    amount = "$quantity $unitOfMeasure"
)