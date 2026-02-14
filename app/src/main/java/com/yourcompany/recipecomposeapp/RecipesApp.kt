import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yourcompany.recipecomposeapp.ScreenId
import com.yourcompany.recipecomposeapp.ui.categories.CategoriesScreen
import com.yourcompany.recipecomposeapp.ui.favorites.FavoritesScreen
import com.yourcompany.recipecomposeapp.ui.navigation.BottomNavigation
import com.yourcompany.recipecomposeapp.ui.recipes.RecipesScreen
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme

@Composable
fun RecipesApp() {
    var currentScreen by remember { mutableStateOf(ScreenId.CATEGORIES) }

    RecipesAppTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        currentScreen = ScreenId.CATEGORIES
                    },
                    onFavoritesClick = {
                        currentScreen = ScreenId.FAVORITES
                    },
                    onRecipesClick = {
                        currentScreen = ScreenId.RECIPES
                    }
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) { paddingValues ->

            when (currentScreen) {
                ScreenId.CATEGORIES ->
                    CategoriesScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )

                ScreenId.FAVORITES -> {
                    FavoritesScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )
                }

                ScreenId.RECIPES ->
                    RecipesScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun RecipeAppPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        RecipesApp()
    }
}

@Composable
@Preview(showBackground = true)
private fun RecipeAppPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        RecipesApp()
    }
}