package com.yourcompany.recipecomposeapp.core.ui

import com.yourcompany.recipecomposeapp.data.Constants
import java.net.URLEncoder

sealed class Destination(val route: String) {
    object Categories : Destination("categories")
    object Favorites : Destination("favorites")

    object Recipes :
        Destination("recipes/{${Constants.KEY_CATEGORY_ID}}/{${Constants.KEY_CATEGORY_TITLE}}/{${Constants.KEY_CATEGORY_IMAGE_URL}}") {

        fun createRecipesRoute(
            categoryId: Int, categoryTitle: String, categoryImageUrl: String
        ): String {
            val encodedTitle = URLEncoder.encode(categoryTitle, "UTF-8")
            val encodedImage = URLEncoder.encode(categoryImageUrl, "UTF-8")
            return "recipes/$categoryId/$encodedTitle/$encodedImage"
        }
    }

    object RecipeDetails : Destination("recipe/{${Constants.KEY_RECIPE_ID}}") {
        fun createDetailsRoute(recipeId: Int) =
            "recipe/$recipeId"
    }

    companion object {
        fun createRecipeDeepLink(recipeId: Int): String {
            return "${Constants.DEEP_LINK_BASE_URL}/recipe/$recipeId"
        }
    }
}
