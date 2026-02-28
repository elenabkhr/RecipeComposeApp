package com.yourcompany.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yourcompany.recipecomposeapp.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.data.repository.RecipeRepositoryStub
import com.yourcompany.recipecomposeapp.ui.categories.model.toUiModel

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (Int) -> Unit,
) {
    val categories = RecipeRepositoryStub
        .getCategories()
        .map { it.toUiModel() }

    Column(modifier = modifier) {
        ScreenHeader(
            text = stringResource(id = R.string.categories_header),
            image =
        )
        LazyVerticalGrid(
            GridCells.Fixed(2),
            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onClick = onCategoryClick,
                )
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
            onCategoryClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoriesScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        CategoriesScreen(
            modifier = Modifier.fillMaxSize(),
            onCategoryClick = {},
        )
    }
}