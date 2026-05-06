package com.yourcompany.recipecomposeapp.features.details.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.features.details.presentation.model.IngredientUiModel
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography
import com.yourcompany.recipecomposeapp.core.utils.shareRecipe
import com.yourcompany.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel

@Composable
fun RecipeDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailsViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            text = uiState.recipe?.title,
            imageUrl = uiState.recipe?.imageUrl,
            showShareButton = true,
            onShareClick = { uiState.recipe?.let { shareRecipe(context, it.id, it.title) } },
            showFavoriteButton = true,
            isFavorite = uiState.isFavorite,
            onFavoriteToggle = { viewModel.toggleFavorite() },
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
                    Log.e("RecipeDetailsScreen", error)
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
                PortionsSelector(uiState.currentPortions) { viewModel.updatePortions(it) }
                IngredientList(uiState.scaledIngredients)

                Text(
                    text = stringResource(id = R.string.cooking_method).uppercase(),
                    style = recipesAppTypography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                MethodsList(uiState.recipe?.method ?: emptyList())
            }
        }
    }
}

@Composable
fun IngredientList(ingredients: List<IngredientUiModel>) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            ingredients.forEachIndexed { index, item ->
                IngredientItem(item)

                if (index < ingredients.lastIndex) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MethodsList(method: List<String>) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {

            method.forEachIndexed { index, item ->
                MethodItem(index, item)

                if (index < method.lastIndex) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    )
                }
            }
        }
    }
}