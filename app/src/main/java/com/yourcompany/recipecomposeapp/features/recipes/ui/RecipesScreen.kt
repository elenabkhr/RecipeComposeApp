package com.yourcompany.recipecomposeapp.features.recipes.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.features.recipes.presentation.RecipesViewModel
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    RecipesContent(uiState, onRecipeClick, modifier)
}

@Composable
fun RecipesContent(
    uiState: RecipesUiState,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ScreenHeader(
            text = uiState.categoryTitle,
            imageUrl = uiState.categoryImageUrl,
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator")) }
            }

            uiState.isError != null || uiState.isEmpty -> {
                uiState.isError?.let { error ->
                    Log.e("RecipesScreen", error)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text =
                            if (uiState.isError != null) stringResource(R.string.data_upload_error)
                            else stringResource(R.string.list_recipes_empty),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = recipesAppTypography.labelLarge,
                        textAlign = TextAlign.Center,
                        modifier =
                            if (uiState.isError != null) Modifier.testTag("error_message")
                            else Modifier.testTag("empty_state")
                    )
                }
            }

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.recipes, key = { it.id }) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipesScreenPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        RecipesScreen(
            viewModel = viewModel(),
            onRecipeClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipesScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        RecipesScreen(
            viewModel = viewModel(),
            onRecipeClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
