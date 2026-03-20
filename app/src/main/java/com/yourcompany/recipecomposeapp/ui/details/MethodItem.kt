package com.yourcompany.recipecomposeapp.ui.details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun MethodItem(
    method: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
    ) {
        Text(
            text = method,
            color = MaterialTheme.colorScheme.onSecondary,
            style = recipesAppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Normal
            ),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MethodItemPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        MethodItem(
            method = "Method",
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MethodItemPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        MethodItem(
            method = "Method",
        )
    }
}