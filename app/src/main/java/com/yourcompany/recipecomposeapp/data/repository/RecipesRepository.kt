package com.yourcompany.recipecomposeapp.data.repository

import com.yourcompany.recipecomposeapp.data.model.CategoryDto
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun getCategories(): Flow<List<CategoryDto>>
    fun getRecipesByCategories(categoryId: Int): Flow<List<RecipeDto>>
    fun getRecipe(recipeId: Int): Flow<RecipeDto?>
}
