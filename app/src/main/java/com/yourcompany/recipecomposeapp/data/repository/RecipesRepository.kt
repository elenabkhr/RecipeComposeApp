package com.yourcompany.recipecomposeapp.data.repository

import com.yourcompany.recipecomposeapp.data.model.CategoryDto
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun getCategories(): Flow<List<CategoryDto>>
    fun getRecipesByCategories(categoryId: Int): Flow<List<RecipeDto>>
    suspend fun getRecipe(recipeId: Int): RecipeDto
}
