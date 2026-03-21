package com.yourcompany.recipecomposeapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortionsSelector(
    currentPortions: Int,
    onPortionsChange: (Int) -> Unit,
) {
    val portionsText = pluralStringResource(
        R.plurals.portions_count,
        currentPortions,
        currentPortions
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(space = 6.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.ingredients_title).uppercase(),
            style = recipesAppTypography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = portionsText,
            style = recipesAppTypography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSecondary
        )

        Slider(
            value = currentPortions.toFloat(),
            onValueChange = { onPortionsChange(it.roundToInt()) },
            valueRange = 1f..5f,
            steps = 4,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.tertiary,
                activeTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
            track = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(4.dp),
                        )
                )
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .width(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(2.dp),
                        ),
                )
            }
        )
    }
}