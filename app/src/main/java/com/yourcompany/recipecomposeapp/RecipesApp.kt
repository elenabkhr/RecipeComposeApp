import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yourcompany.recipecomposeapp.data.DEEP_LINK_SCHEME
import com.yourcompany.recipecomposeapp.data.repository.RecipeRepositoryStub.getRecipeById
import com.yourcompany.recipecomposeapp.ui.categories.CategoriesScreen
import com.yourcompany.recipecomposeapp.ui.details.RecipeDetailsScreen
import com.yourcompany.recipecomposeapp.ui.favorites.FavoritesScreen
import com.yourcompany.recipecomposeapp.ui.navigation.BottomNavigation
import com.yourcompany.recipecomposeapp.ui.navigation.Destination
import com.yourcompany.recipecomposeapp.ui.recipes.RecipesScreen
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.utils.FavoritesPrefsManager
import kotlinx.coroutines.delay

@Composable
fun RecipesApp(deepLinkIntent: Intent?) {
    val navController = rememberNavController()

    LaunchedEffect(deepLinkIntent) {
        deepLinkIntent?.data?.let { uri ->
            val recipeId: Int? = when (uri.scheme) {
                DEEP_LINK_SCHEME ->
                    if (uri.host == "recipe") uri.pathSegments[0].toIntOrNull() else null

                "https", "http" ->
                    if (uri.pathSegments[0] == "recipe") uri.pathSegments[1].toIntOrNull() else null

                else -> null
            }
            if (recipeId != null) {
                delay(100)
                navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
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
                        onCategoryClick = { categoryId, categoryTitle ->
                            navController.navigate(
                                Destination.Recipes.createRoute(categoryId, categoryTitle)
                            )
                        }
                    )
                }

                composable(route = Destination.Favorites.route) {
                    FavoritesScreen()
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(
                        navArgument("categoryId") { type = NavType.IntType },
                        navArgument("categoryTitle") { type = NavType.StringType }),
                ) { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getInt("categoryId")
                        ?: error("Category ID is required")
                    val categoryTitle = backStackEntry.arguments?.getString("categoryTitle")
                        ?: error("Category title is required")
                    RecipesScreen(
                        categoryId = categoryId,
                        categoryTitle = categoryTitle,
                        onRecipeClick = { recipeId ->
                            navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                        }
                    )
                }

                composable(
                    route = Destination.RecipeDetails.route,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getInt("id") ?: 0
                    val recipe = getRecipeById(recipeId)

                    val context = LocalContext.current
                    val prefs = remember { FavoritesPrefsManager(context) }

                    var isFavorite by remember(recipeId) {
                        mutableStateOf(prefs.isFavorite(recipeId))
                    }

                    recipe?.let {
                        RecipeDetailsScreen(
                            recipe = it,
                            isFavorite = isFavorite,
                            onFavoriteToggle = {
                                if (isFavorite) {
                                    prefs.removeFromFavorites(recipeId)
                                } else {
                                    prefs.addToFavorites(recipeId)
                                }
                                isFavorite = !isFavorite
                            }
                        )
                    }
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