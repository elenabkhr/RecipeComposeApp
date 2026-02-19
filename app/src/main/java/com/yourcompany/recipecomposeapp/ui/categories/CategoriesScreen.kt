package com.yourcompany.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.yourcompany.recipecomposeapp.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader

@Composable
fun CategoriesScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        ScreenHeader(
            text = stringResource(id = R.string.categories_header),
            painter = painterResource(id = R.drawable.bcg_categories)
        )
        CategoriesItem()
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoriesScreenPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        CategoriesScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoriesScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        CategoriesScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}