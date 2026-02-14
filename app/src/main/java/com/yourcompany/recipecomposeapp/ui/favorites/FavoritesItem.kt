package com.yourcompany.recipecomposeapp.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun FavoritesItem() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Вы еще не добавили ни одного рецепта в избранное",
            color = MaterialTheme.colorScheme.onSecondary,
            style = recipesAppTypography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesItemPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        FavoritesItem()
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesItemPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        FavoritesItem()
    }
}