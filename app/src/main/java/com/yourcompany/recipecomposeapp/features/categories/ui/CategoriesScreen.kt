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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.yourcompany.recipecomposeapp.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.data.model.CategoryDto
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepository
import com.yourcompany.recipecomposeapp.features.categories.presentation.CategoriesViewModel
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.collections.emptyList

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (Int, String, String) -> Unit,
    repository: RecipesRepository,
) {
    val viewModel: CategoriesViewModel = remember { CategoriesViewModel(repository) }
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier) {
        ScreenHeader(
            text = stringResource(id = R.string.categories),
            painter = painterResource(id = R.drawable.bcg_categories),
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
            repository = object : RecipesRepository {
                override fun getCategories(): Flow<List<CategoryDto>> {
                    return flowOf(emptyList())
                }

                override fun getRecipesByCategories(categoryId: Int): Flow<List<RecipeDto>> {
                    return flowOf(emptyList())
                }

                override suspend fun getRecipe(recipeId: Int): RecipeDto = RecipeDto(
                    id = 0,
                    title = "title",
                    categoryId = 1,
                    ingredients = emptyList(),
                    method = emptyList(),
                    imageUrl = "imageUrl"
                )
            }
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
            repository = object : RecipesRepository {
                override fun getCategories(): Flow<List<CategoryDto>> {
                    return flowOf(emptyList())
                }

                override fun getRecipesByCategories(categoryId: Int): Flow<List<RecipeDto>> {
                    return flowOf(emptyList())
                }

                override suspend fun getRecipe(recipeId: Int): RecipeDto = RecipeDto(
                    id = 0,
                    title = "title",
                    categoryId = 1,
                    ingredients = emptyList(),
                    method = emptyList(),
                    imageUrl = "imageUrl"
                )
            }
        )
    }
}