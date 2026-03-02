package com.yourcompany.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.ui.categories.model.CategoryUiModel
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun CategoryItem(
    category: CategoryUiModel,
    onClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),

        onClick = { onClick(category.id, category.title) },
    ) {
        Column(modifier.fillMaxSize()) {

            AsyncImage(
                model = category.imageUrl,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.bcg_placeholder),
                error = painterResource(id = R.drawable.bcg_error),
                modifier = modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = category.title.uppercase(),
                modifier = modifier.padding(start = 8.dp, top = 8.dp),
                style = recipesAppTypography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = category.description,
                modifier = modifier.padding(8.dp),
                style = recipesAppTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryScreenPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        CategoryItem(
            category = CategoryUiModel(
                id = 1,
                title = "",
                description = "",
                imageUrl = ""
            ),
            onClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryScreenPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        CategoryItem(
            category = CategoryUiModel(
                id = 1,
                title = "",
                description = "",
                imageUrl = ""
            ),
            onClick = { _, _ -> }
        )
    }
}