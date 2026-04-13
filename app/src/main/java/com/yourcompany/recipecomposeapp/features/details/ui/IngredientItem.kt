package com.yourcompany.recipecomposeapp.features.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.features.details.presentation.model.IngredientUiModel
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun IngredientItem(
    ingredient: IngredientUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = ingredient.name.uppercase(),
            modifier = Modifier.width(175.dp),
            color = MaterialTheme.colorScheme.onSecondary,
            style = recipesAppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Normal
            ),
        )

        Text(
            text = "${formatQuantity(ingredient.quantity)} ${ingredient.unit}".uppercase(),
            color = MaterialTheme.colorScheme.onSecondary,
            style = recipesAppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Normal
            ),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun IngredientItemPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        IngredientItem(
            ingredient = IngredientUiModel(
                name = "",
                quantity = 1.0,
                unit = "",
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun IngredientItemPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        IngredientItem(
            ingredient = IngredientUiModel(
                name = "",
                quantity = 1.0,
                unit = "",
            )
        )
    }
}