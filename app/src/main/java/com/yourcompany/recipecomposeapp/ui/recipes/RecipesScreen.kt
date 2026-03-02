package com.yourcompany.recipecomposeapp.ui.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.key
import androidx.compose.ui.res.painterResource
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.data.repository.RecipeRepositoryStub
import com.yourcompany.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.yourcompany.recipecomposeapp.ui.recipes.model.toUiModel
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme

@Composable
fun RecipesScreen(
    categoryId: Int,
    categoryTitle: String,
    modifier: Modifier = Modifier
) {
    var recipes by remember { mutableStateOf<List<RecipeUiModel>>(emptyList()) }

    LaunchedEffect(categoryId) {
        recipes = RecipeRepositoryStub
            .getRecipesByCategoryId(categoryId)
            .map { dto -> dto.toUiModel() }
    }

    Column(modifier = modifier) {
        ScreenHeader(
            text = categoryTitle,
            painter = painterResource(id = R.drawable.bcg_categories)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recipes, key = { it.id }) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    modifier = modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                    onClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipesScreenPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        RecipesScreen(
            categoryId = 0,
            categoryTitle = "",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipesScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        RecipesScreen(
            categoryId = 0,
            categoryTitle = "",
            modifier = Modifier.fillMaxSize(),
        )
    }
}
