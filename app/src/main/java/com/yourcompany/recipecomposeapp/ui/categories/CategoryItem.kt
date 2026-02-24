package com.yourcompany.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun CategoryItem(
    onClick: () -> Unit,
    urlImage: String,
    title: String,
    description: String,
) {
    Card(
        modifier = Modifier
            .width(156.dp)
            .height(220.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
    ) {
        Column {
            AsyncImage(
                model = urlImage,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.bcg_placeholder),
                error = painterResource(id = R.drawable.bcg_error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = title.uppercase(),
                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                style = recipesAppTypography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = description,
                modifier = Modifier.padding(8.dp),
                style = recipesAppTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}