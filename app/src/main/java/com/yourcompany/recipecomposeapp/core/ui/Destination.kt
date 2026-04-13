package com.yourcompany.recipecomposeapp.core.ui

import com.yourcompany.recipecomposeapp.data.DEEP_LINK_BASE_URL

sealed class Destination(val route: String) {
    object Categories : Destination("categories")
    object Favorites : Destination("favorites")
    object Recipes : Destination("recipes/{categoryId}/{categoryTitle}/{categoryImageUrl}") {
        fun createRoute(categoryId: Int, categoryTitle: String, categoryImageUrl: String) =
            "recipes/$categoryId/$categoryTitle/$categoryImageUrl"
    }

    object RecipeDetails : Destination("recipe/{id}") {
        fun createRoute(id: Int) =
            "recipe/$id"
    }

    companion object {
        fun createRecipeDeepLink(recipeId: Int): String {
            return "$DEEP_LINK_BASE_URL/recipe/$recipeId"
        }
    }
}