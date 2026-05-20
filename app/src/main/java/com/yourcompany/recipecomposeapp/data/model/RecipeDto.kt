package com.yourcompany.recipecomposeapp.data.model

import com.yourcompany.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    val categoryId: Int = 0,
    val ingredients: List<IngredientDto>,
    val method: List<String>,
    val imageUrl: String,
)

fun RecipeDto.toEntity() = RecipeEntity(
    id = id,
    title = title,
    categoryId = categoryId,
    ingredients = Json.encodeToString(ingredients),
    method = method.joinToString(separator = "|||"),
    imageUrl = imageUrl,
)

fun RecipeEntity.toDto() = RecipeDto(
    id = id,
    title = title,
    categoryId = categoryId,
    ingredients = Json.decodeFromString<List<IngredientDto>>(ingredients),
    method = method.split("|||"),
    imageUrl = imageUrl,
)
