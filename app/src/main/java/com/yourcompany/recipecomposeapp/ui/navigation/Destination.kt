package com.yourcompany.recipecomposeapp.ui.navigation

import com.yourcompany.recipecomposeapp.data.DEEP_LINK_BASE_URL

sealed class Destination(val route: String) {
    object Categories : Destination("categories")
    object Favorites : Destination("favorites")
    object Recipes : Destination("recipes/{categoryId}/{categoryTitle}") {
        fun createRoute(categoryId: Int, categoryTitle: String) =
            "recipes/$categoryId/$categoryTitle"
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