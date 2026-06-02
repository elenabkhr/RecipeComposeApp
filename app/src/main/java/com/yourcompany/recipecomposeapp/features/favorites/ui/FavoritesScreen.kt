package com.yourcompany.recipecomposeapp.features.favorites.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.features.favorites.presentation.FavoritesViewModel
import com.yourcompany.recipecomposeapp.features.recipes.ui.RecipeItem
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun FavoritesScreen(
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: FavoritesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier) {
        ScreenHeader(
            text = stringResource(id = R.string.favorites),
            painter = painterResource(id = R.drawable.bcg_favorites),
        )
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator() }
            }

            uiState.isError != null -> {
                uiState.isError?.let { error ->
                    Log.e("FavoritesScreen", error)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.data_upload_error),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = recipesAppTypography.labelLarge,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            else -> {
                if (uiState.recipes.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(uiState.recipes, key = { it.id }) { recipe ->
                            RecipeItem(
                                recipe = recipe,
                                onClick = { onRecipeClick(recipe.id) }
                            )
                        }
                    }
                } else if (uiState.isInitialized) {
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
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        FavoritesScreen(
            onRecipeClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        FavoritesScreen(
            onRecipeClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
