package com.yourcompany.recipecomposeapp.ui.favorites

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.data.datastore.FavoriteDataStoreManager
import com.yourcompany.recipecomposeapp.data.repository.RecipeRepositoryStub
import com.yourcompany.recipecomposeapp.ui.recipes.RecipeItem
import com.yourcompany.recipecomposeapp.ui.recipes.model.toUiModel
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography
import kotlinx.coroutines.flow.map

@Composable
fun FavoritesScreen(
    recipesRepository: RecipeRepositoryStub,
    favoriteDataStoreManager: FavoriteDataStoreManager,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val recipeFavorites by favoriteDataStoreManager
        .getFavoriteIdsFlow().map { ids ->
            ids.mapNotNull {
                try {
                    recipesRepository.getRecipeById(it.toIntOrNull())
                } catch (e: NumberFormatException) {
                    Log.e("FavoritesScreen", "Failed to get recipes by ids", e)
                    null
                }
            }
        }
        .collectAsState(initial = emptyList())

    val recipes = recipeFavorites.map { dto -> dto.toUiModel() }

    Column(modifier = modifier) {
        ScreenHeader(
            text = stringResource(id = R.string.favorites),
            painter = painterResource(id = R.drawable.bcg_favorites),
        )

        if (recipes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(recipes, key = { it.id }) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe.id) }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_favorite_recipe),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = recipesAppTypography.labelLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        FavoritesScreen(
            recipesRepository = RecipeRepositoryStub,
            favoriteDataStoreManager = FavoriteDataStoreManager(LocalContext.current),
            onRecipeClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        FavoritesScreen(
            recipesRepository = RecipeRepositoryStub,
            favoriteDataStoreManager = FavoriteDataStoreManager(LocalContext.current),
            onRecipeClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
