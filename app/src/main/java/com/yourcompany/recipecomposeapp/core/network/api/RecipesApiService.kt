package com.yourcompany.recipecomposeapp.core.network.api

import com.yourcompany.recipecomposeapp.data.model.CategoryDto
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipesApiService {

    @GET("category")
    fun getCategories(): Call<List<CategoryDto>>

    @GET("category/{id}/recipes")
    fun getRecipesByCategory(@Path("id") categoryId: Int): Call<List<RecipeDto>>
}