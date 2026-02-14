package com.yourcompany.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme

@Composable
fun CategoriesItem() {
    Text(
        text = "Заглушка категорий",
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoriesItemPreview() {
    RecipesAppTheme {
        CategoriesItem()
    }
}