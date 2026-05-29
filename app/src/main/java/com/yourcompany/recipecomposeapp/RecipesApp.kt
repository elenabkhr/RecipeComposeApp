package com.yourcompany.recipecomposeapp

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yourcompany.recipecomposeapp.features.categories.ui.CategoriesScreen
import com.yourcompany.recipecomposeapp.features.details.ui.RecipeDetailsScreen
import com.yourcompany.recipecomposeapp.features.favorites.ui.FavoritesScreen
import com.yourcompany.recipecomposeapp.core.ui.BottomNavigation
import com.yourcompany.recipecomposeapp.core.ui.Destination
import com.yourcompany.recipecomposeapp.data.Constants
import com.yourcompany.recipecomposeapp.di.RecipeApplication
import com.yourcompany.recipecomposeapp.di.RecipeDetailsViewModelFactory
import com.yourcompany.recipecomposeapp.di.RecipesViewModelFactory
import com.yourcompany.recipecomposeapp.features.recipes.ui.RecipesScreen
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import kotlinx.coroutines.delay

@Composable
fun RecipesApp(deepLinkIntent: Intent?) {
    val navController = rememberNavController()

    val appContainer = (LocalContext.current.applicationContext as RecipeApplication).appContainer
    val application = LocalContext.current.applicationContext as Application

    LaunchedEffect(deepLinkIntent) {
        deepLinkIntent?.data?.let { uri ->
            val recipeId: Int? = when (uri.scheme) {
                Constants.DEEP_LINK_SCHEME ->
                    if (uri.host == "recipe") uri.pathSegments[0].toIntOrNull() else null

                "https", "http" ->
                    if (uri.pathSegments[0] == "recipe") uri.pathSegments[1].toIntOrNull() else null

                else -> null
            }
            if (recipeId != null) {
                delay(100)
                navController.navigate(Destination.RecipeDetails.createDetailsRoute(recipeId))
            }
        }
    }

    RecipesAppTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        navController.navigate(Destination.Categories.route)
                    },
                    onFavoritesClick = {
                        navController.navigate(Destination.Favorites.route)
                    },
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = Destination.Categories.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = Destination.Categories.route) {
                    CategoriesScreen(
                        onCategoryClick = { categoryId, categoryTitle, categoryImageUrl ->
                            navController.navigate(
                                Destination.Recipes.createRecipesRoute(
                                    categoryId,
                                    categoryTitle,
                                    categoryImageUrl,
                                )
                            )
                        }
                    )
                }

                composable(route = Destination.Favorites.route) {
                    FavoritesScreen(
                        onRecipeClick = { recipeId ->
                            navController.navigate(
                                Destination.RecipeDetails.createDetailsRoute(recipeId)
                            )
                        },
                    )
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(
                        navArgument(Constants.KEY_CATEGORY_ID) { type = NavType.IntType },
                        navArgument(Constants.KEY_CATEGORY_TITLE) { type = NavType.StringType },
                        navArgument(Constants.KEY_CATEGORY_IMAGE_URL) {
                            type = NavType.StringType
                        }),
                ) { backStackEntry ->
                    val savedStateHandle = remember(backStackEntry) {
                        SavedStateHandle().apply {
                            backStackEntry.arguments?.let { bundle ->
                                bundle.keySet().forEach { key -> set(key, bundle.get(key)) }
                            }
                        }
                    }
                    RecipesScreen(
                        viewModel = remember {
                            RecipesViewModelFactory(
                                savedStateHandle = savedStateHandle,
                                appContainer.recipesRepository
                            ).create()
                        },
                        onRecipeClick = { recipeId ->
                            navController.navigate(
                                Destination.RecipeDetails.createDetailsRoute(recipeId)
                            )
                        }
                    )
                }

                composable(
                    route = Destination.RecipeDetails.route,
                    arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val savedStateHandle = remember(backStackEntry) {
                        SavedStateHandle().apply {
                            backStackEntry.arguments?.let { bundle ->
                                bundle.keySet().forEach { key -> set(key, bundle.get(key)) }
                            }
                        }
                    }
                    RecipeDetailsScreen(viewModel = remember {
                        RecipeDetailsViewModelFactory(
                            application = application,
                            savedStateHandle = savedStateHandle,
                            appContainer.recipesRepository
                        ).create()
                    })
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun RecipeAppPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        RecipesApp(null)
    }
}

@Composable
@Preview(showBackground = true)
private fun RecipeAppPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        RecipesApp(null)
    }
}