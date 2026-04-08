package com.yourcompany.recipecomposeapp.features.details.presentation.model

import androidx.compose.runtime.Immutable
import com.yourcompany.recipecomposeapp.data.model.IngredientDto

@Immutable
data class IngredientUiModel(
    val name: String,
    val quantity: Double,
    val unit: String
)

fun IngredientDto.toUiModel() = IngredientUiModel(
    name = description,
    quantity = quantity.toDoubleOrNull() ?: 0.0,
    unit = unitOfMeasure
)