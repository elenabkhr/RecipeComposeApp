package com.yourcompany.recipecomposeapp.data.repository

import android.util.Log
import com.yourcompany.recipecomposeapp.core.network.api.RecipesApiService
import com.yourcompany.recipecomposeapp.data.model.CategoryDto
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class RecipesRepositoryImpl(private val apiService: RecipesApiService) : RecipesRepository {
    override suspend fun getCategories(): List<CategoryDto> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getCategories()
            } catch (e: IOException) {
                Log.e("!!!", "Ошибка сетевого запроса `getCategories`", e)
                emptyList()
            }
        }
    }

    override suspend fun getRecipesByCategories(categoryId: Int): List<RecipeDto> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getRecipesByCategory(categoryId)
            } catch (e: IOException) {
                Log.e("!!!", "Ошибка сетевого запроса `getRecipesByCategories`", e)
                emptyList()
            }
        }
    }

    override suspend fun getRecipe(recipeId: Int): RecipeDto {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getRecipe(recipeId)
            } catch (e: IOException) {
                throw IOException("Ошибка сетевого запроса `getRecipe`", e)
            }
        }
    }
}
