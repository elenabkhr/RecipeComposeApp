package com.yourcompany.recipecomposeapp.features.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yourcompany.recipecomposeapp.R
import com.yourcompany.recipecomposeapp.core.ui.ScreenHeader
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import com.yourcompany.recipecomposeapp.features.details.presentation.model.IngredientUiModel
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.toUiModel
import com.yourcompany.recipecomposeapp.ui.theme.recipesAppTypography
import com.yourcompany.recipecomposeapp.core.utils.shareRecipe
import kotlin.collections.map

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeDto,
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
) {
    val recipe = recipe.toUiModel()
    val context = LocalContext.current

    var currentPortions by rememberSaveable { mutableIntStateOf(recipe.servings) }

    val scaledIngredients = remember(recipe.ingredients, currentPortions) {
        val multiplier = currentPortions.toDouble() / recipe.servings

        recipe.ingredients.map { ingredient ->
            ingredient.copy(quantity = (ingredient.quantity * multiplier))
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            text = recipe.title,
            painter = painterResource(id = R.drawable.bcg_categories),
            showShareButton = true,
            onShareClick = { shareRecipe(context, recipe.id, recipe.title) },
            showFavoriteButton = true,
            isFavorite = isFavorite,
            onFavoriteToggle = onFavoriteToggle,
        )

        PortionsSelector(currentPortions) { newValue -> currentPortions = newValue }

        IngredientList(scaledIngredients)

        Text(
            text = stringResource(id = R.string.cooking_method).uppercase(),
            style = recipesAppTypography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        MethodsList(recipe.method)
    }
}

@Composable
fun IngredientList(ingredients: List<IngredientUiModel>) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            ingredients.forEachIndexed { index, item ->
                IngredientItem(item)

                if (index < ingredients.lastIndex) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MethodsList(method: List<String>) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {

            method.forEachIndexed { index, item ->
                MethodItem(index, item)

                if (index < method.lastIndex) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    )
                }
            }
        }
    }
}