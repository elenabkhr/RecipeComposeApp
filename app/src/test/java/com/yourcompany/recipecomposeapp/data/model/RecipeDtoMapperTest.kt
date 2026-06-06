package com.yourcompany.recipecomposeapp.data.model

import com.yourcompany.recipecomposeapp.data.Constants
import com.yourcompany.recipecomposeapp.features.details.presentation.model.IngredientUiModel
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.toUiModel
import fixtures.RecipeTestFixtures
import fixtures.RecipeTestFixtures.createIngredientDto
import org.junit.Assert.*
import org.junit.Test

class RecipeDtoMapperTest {
    @Test
    fun `maps DTO to UI model correctly`() {
        val recipeDto = RecipeTestFixtures.createRecipeDto(
            id = 2,
            title = "Pasta Carbonara",
            ingredients = listOf(
                createIngredientDto(
                    quantity = "200",
                    unitOfMeasure = "г",
                    description = "Паста"
                )
            ),
            method = listOf("Отварить", "Смешать"),
            imageUrl = "pasta.jpg"
        )

        val expected = RecipeUiModel(
            id = 2,
            title = "Pasta Carbonara",
            ingredients = listOf(IngredientUiModel(name = "Паста", quantity = 200.0, unit = "г")),
            method = listOf("Отварить", "Смешать"),
            imageUrl = Constants.IMAGES_BASE_URL + "pasta.jpg",
        )

        assertEquals(expected, recipeDto.toUiModel())
    }

    @Test
    fun `preserves full imageUrl starting with http`() {
        val dto =
            RecipeTestFixtures.createRecipeDto(imageUrl = Constants.IMAGES_BASE_URL + "pasta.jpg")
        val result = dto.toUiModel()
        assertEquals(Constants.IMAGES_BASE_URL + "pasta.jpg", result.imageUrl)
    }

    @Test
    fun `prepends base url to relative imageUrl`() {
        val dto =
            RecipeTestFixtures.createRecipeDto(imageUrl = "pasta.jpg")
        val result = dto.toUiModel()

        assertEquals(Constants.IMAGES_BASE_URL + "pasta.jpg", result.imageUrl)
    }
}