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
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yourcompany.recipecomposeapp.core.network.NetworkConfig
import com.yourcompany.recipecomposeapp.core.network.api.RecipesApiService
import com.yourcompany.recipecomposeapp.features.categories.ui.CategoriesScreen
import com.yourcompany.recipecomposeapp.features.details.ui.RecipeDetailsScreen
import com.yourcompany.recipecomposeapp.features.favorites.ui.FavoritesScreen
import com.yourcompany.recipecomposeapp.core.ui.BottomNavigation
import com.yourcompany.recipecomposeapp.core.ui.Destination
import com.yourcompany.recipecomposeapp.data.Constants
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepositoryImpl
import com.yourcompany.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel
import com.yourcompany.recipecomposeapp.features.favorites.presentation.FavoritesViewModel
import com.yourcompany.recipecomposeapp.features.recipes.presentation.RecipesViewModel
import com.yourcompany.recipecomposeapp.features.recipes.ui.RecipesScreen
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Composable
fun RecipesApp(deepLinkIntent: Intent?) {
    val navController = rememberNavController()

    val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val apiService: RecipesApiService = retrofit.create(RecipesApiService::class.java)
    val repository = remember { RecipesRepositoryImpl(apiService) }

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
                        repository = repository,
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

                composable(route = Destination.Favorites.route) { backStackEntry ->
                    val context = LocalContext.current
                    val viewModel: FavoritesViewModel = remember(backStackEntry) {
                        FavoritesViewModel(context.applicationContext as Application, repository)
                    }

                    FavoritesScreen(
                        viewModel = viewModel,
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
                    val savedStateHandle = backStackEntry.savedStateHandle
                    val viewModel: RecipesViewModel = remember(backStackEntry) {
                        RecipesViewModel(savedStateHandle, repository)
                    }
                    RecipesScreen(
                        viewModel = viewModel,
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
                    val context = LocalContext.current
                    val savedStateHandle = backStackEntry.savedStateHandle
                    val viewModel = remember(backStackEntry) {
                        RecipeDetailsViewModel(
                            context.applicationContext as Application,
                            savedStateHandle,
                            repository,
                        )
                    }
                    RecipeDetailsScreen(viewModel = viewModel)
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