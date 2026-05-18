package com.yourcompany.recipecomposeapp.data.model

import com.yourcompany.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    val categoryId: Int,
    val ingredients: List<IngredientDto>,
    val method: List<String>,
    val imageUrl: String,
)

fun RecipeDto.toEntity() = RecipeEntity(
    id = id,
    title = title,
    categoryId = categoryId,
    ingredients = ingredients.joinToString(separator = "|||"),
    method = method.joinToString(separator = "|||"),
    imageUrl = imageUrl,
)

fun RecipeEntity.toDto() = RecipeDto(
    id = id,
    title = title,
    categoryId = categoryId,
    ingredients = ingredients.split("|||").map { IngredientDto() }, ///
    method = method.split("|||"),
    imageUrl = imageUrl,
)
