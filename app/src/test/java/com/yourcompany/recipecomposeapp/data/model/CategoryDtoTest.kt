package com.yourcompany.recipecomposeapp.data.model

import com.yourcompany.recipecomposeapp.data.Constants
import com.yourcompany.recipecomposeapp.features.categories.presentation.model.toUiModel
import fixtures.CategoryTestFixtures
import org.junit.Assert.*
import org.junit.Test

class CategoryDtoTest {
    @Test
    fun `mapper maps empty title correctly`() {
        val dto = CategoryTestFixtures.createCategoryDto(title = "")
        val result = dto.toUiModel()

        assertEquals("", result.title)
    }

    @Test
    fun `mapper preserves very long description`() {
        val longDescription = "Утренние блюда".repeat(1000)

        val dto = CategoryTestFixtures.createCategoryDto(description = longDescription)
        val result = dto.toUiModel()

        assertEquals(longDescription, result.description)
    }

    @Test
    fun `preserves full imageUrl starting with http`() {
        val dto =
            CategoryTestFixtures.createCategoryDto(imageUrl = Constants.IMAGES_BASE_URL + "pasta.jpg")
        val result = dto.toUiModel()
        assertEquals(Constants.IMAGES_BASE_URL + "pasta.jpg", result.imageUrl)
    }

    @Test
    fun `prepends base url to relative imageUrl`() {
        val dto =
            CategoryTestFixtures.createCategoryDto(imageUrl = "pasta.jpg")
        val result = dto.toUiModel()

        assertEquals(Constants.IMAGES_BASE_URL + "pasta.jpg", result.imageUrl)
    }
}
