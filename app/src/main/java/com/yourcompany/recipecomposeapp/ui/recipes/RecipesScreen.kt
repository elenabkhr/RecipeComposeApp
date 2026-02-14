package com.yourcompany.recipecomposeapp.ui.recipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme

@Composable
fun RecipesScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ScreenHeader(
            text = stringResource(id = R.string.recipes_header),
        )
        RecipesItem()
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipesScreenPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        RecipesScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipesScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        RecipesScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}
