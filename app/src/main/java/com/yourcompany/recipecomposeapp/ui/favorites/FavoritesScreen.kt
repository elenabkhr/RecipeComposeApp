package com.yourcompany.recipecomposeapp.ui.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ScreenHeader(
            text = stringResource(id = R.string.favorites_header),
            painterResource(id = R.drawable.bcg_favorites)
        )
        FavoritesItem()
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        FavoritesScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        FavoritesScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}

