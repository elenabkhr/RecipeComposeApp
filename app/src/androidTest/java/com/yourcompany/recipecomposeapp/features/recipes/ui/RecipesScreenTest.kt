package com.yourcompany.recipecomposeapp.features.recipes.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsLoadingState() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(isLoading = true),
                onRecipeClick = {},
            )
        }
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun showsErrorState() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(isError = "Network error"),
                onRecipeClick = {},
            )
        }
        composeTestRule.onNodeWithText("Ошибка загрузки данных").assertIsDisplayed()
    }

    @Test
    fun showsEmptyState() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(),
                onRecipeClick = {},
            )
        }
        composeTestRule.onNodeWithTag("empty_state").assertIsDisplayed()
    }

    @Test
    fun displaysRecipeList() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(
                    recipes = listOf(
                        RecipeUiModel(
                            id = 1,
                            title = "Pasta",
                            ingredients = emptyList(),
                            method = emptyList(),
                            imageUrl = ""
                        )
                    )
                ),
                onRecipeClick = {},
            )
        }
        composeTestRule.onNodeWithText("Pasta".uppercase()).assertIsDisplayed()
    }
}