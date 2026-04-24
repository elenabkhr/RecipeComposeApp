package com.yourcompany.recipecomposeapp.features.categories.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.yourcompany.recipecomposeapp.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.features.categories.presentation.CategoriesViewModel
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (Int, String, String) -> Unit,
) {
    val viewModel: CategoriesViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier) {
        ScreenHeader(
            text = stringResource(id = R.string.categories),
            painter = painterResource(id = R.drawable.bcg_categories),
        )

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) { CircularProgressIndicator() }
            }

            uiState.isError != null -> {
                uiState.isError?.let { error ->
                    Log.e("CategoriesScreen", error)
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

            else -> LazyVerticalGrid(
                GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.categories, key = { it.id }) { category ->
                    CategoryItem(
                        category = category,
                        onClick = onCategoryClick,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoriesScreenPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        CategoriesScreen(
            modifier = Modifier.fillMaxSize(),
            onCategoryClick = { _, _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoriesScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        CategoriesScreen(
            modifier = Modifier.fillMaxSize(),
            onCategoryClick = { _, _, _ -> },
        )
    }
}