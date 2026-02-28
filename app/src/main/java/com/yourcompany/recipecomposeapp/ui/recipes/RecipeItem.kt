package com.yourcompany.recipecomposeapp.ui.recipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme

@Composable
fun RecipeItem(
    recipe: RecipeUiModel,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        onClick = { onClick(recipe.id) }
    ) {
        Column(modifier.fillMaxSize()) {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.bcg_placeholder),
                error = painterResource(id = R.drawable.bcg_error),
                modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipesItemPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        RecipeItem(
            recipe = RecipeUiModel(
                id = 1,
                title = "",
                ingredients = listOf(),
                method = listOf(),
                imageUrl = "",
                isFavorite = false
            ),
            onClick = {},
            Modifier
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun RecipesItemPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        RecipeItem(
            recipe = RecipeUiModel(
                id = 1,
                title = "",
                ingredients = listOf(),
                method = listOf(),
                imageUrl = "",
                isFavorite = false
            ),
            onClick = {},
            Modifier
        )
    }
}