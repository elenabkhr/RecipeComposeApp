package com.yourcompany.recipecomposeapp.ui.navigation

import com.yourcompany.recipecomposeapp.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun BottomNavigation(
    onCategoriesClick: () -> Unit,
    onFavoritesClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.Center,
    ) {
        CategoriesButton(onClick = onCategoriesClick, Modifier.weight(1f))

        Spacer(modifier = Modifier.width(4.dp))

        FavoritesButton(onClick = onFavoritesClick, Modifier.weight(1f))
    }
}

@Composable
fun CategoriesButton(onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            MaterialTheme.colorScheme.tertiary
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
    ) {
        Text(
            "КАТЕГОРИИ",
            style = recipesAppTypography.titleMedium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun FavoritesButton(onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            MaterialTheme.colorScheme.error
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
    ) {
        Text(
            "ИЗБРАННОЕ",
            style = recipesAppTypography.titleMedium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_heart_empty),
            contentDescription = "Иконка сердце",
            modifier = Modifier.size(24.dp),
            tint = androidx.compose.ui.graphics.Color.Unspecified
        )
    }
}

@Composable
@Preview(showBackground = true)
fun BottomNavigationPreviewLight() {
    RecipesAppTheme(darkTheme = false) {
        BottomNavigation(
            onCategoriesClick = {},
            onFavoritesClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun BottomNavigationPreviewDark() {
    RecipesAppTheme(darkTheme = true) {
        BottomNavigation(
            onCategoriesClick = {},
            onFavoritesClick = {}
        )
    }
}