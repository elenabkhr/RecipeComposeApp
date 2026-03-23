package com.yourcompany.recipecomposeapp.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.ui.theme.RecipesAppTheme
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography

@Composable
fun ScreenHeader(
    text: String,
    painter: Painter = painterResource(id = R.drawable.bcg_placeholder),
    showShareButton: Boolean = false,
    onShareClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(224.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        if (showShareButton) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                IconButton(onClick = onShareClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = stringResource(id = R.string.icon_share),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(30.dp),
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.padding(all = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text.uppercase(),
                    style = recipesAppTypography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenHeaderPreview() {
    RecipesAppTheme {
        ScreenHeader(
            text = ""
        )
    }
}
