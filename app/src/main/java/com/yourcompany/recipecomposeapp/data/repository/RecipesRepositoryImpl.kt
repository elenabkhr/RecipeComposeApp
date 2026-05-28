package com.yourcompany.recipecomposeapp.data.repository

import android.util.Log
import com.yourcompany.recipecomposeapp.core.network.api.RecipesApiService
import com.yourcompany.recipecomposeapp.data.database.RecipesDatabase
import com.yourcompany.recipecomposeapp.data.model.CategoryDto
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import com.yourcompany.recipecomposeapp.data.model.toDto
import com.yourcompany.recipecomposeapp.data.model.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

class RecipesRepositoryImpl(
    private val apiService: RecipesApiService,
    database: RecipesDatabase,
) : RecipesRepository {
    private val categoryDao = database.categoryDao()
    private val recipeDao = database.recipeDao()

    override fun getCategories(): Flow<List<CategoryDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fresh = apiService.getCategories()
                categoryDao.insertCategories(fresh.map { it.toEntity() })
                Log.d("!!!", "Обновлено ${fresh.size} категорий")
            } catch (e: IOException) {
                Log.e("!!!", "Ошибка сетевого запроса `getCategories`", e)
            }
        }
        return categoryDao.getAllCategories().map { entities -> entities.map { it.toDto() } }
    }

    override fun getRecipesByCategories(categoryId: Int): Flow<List<RecipeDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fresh = apiService.getRecipesByCategory(categoryId)
                recipeDao.insertRecipes(fresh.map { it.toEntity(categoryId) })
                Log.d("!!!", "Обновлено ${fresh.size} рецептов")
            } catch (e: IOException) {
                Log.e("!!!", "Ошибка сетевого запроса `getRecipesByCategories`", e)
            }
        }
        return recipeDao.getRecipesByCategory(categoryId)
            .map { entities -> entities.map { it.toDto() } }
    }

    override fun getRecipe(recipeId: Int): Flow<RecipeDto?> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categoryId = recipeDao.getRecipeById(recipeId).first()?.categoryId
                val fresh = apiService.getRecipe(recipeId)
                recipeDao.insertRecipe(fresh.toEntity(categoryId))
                Log.d("!!!", "Детали рецепта получены из API")
            } catch (e: IOException) {
                Log.e("!!!", "Ошибка обновления: ${e.message}")
            }
        }
        return recipeDao.getRecipeById(recipeId).map { entity -> entity?.toDto() }
    }
}
