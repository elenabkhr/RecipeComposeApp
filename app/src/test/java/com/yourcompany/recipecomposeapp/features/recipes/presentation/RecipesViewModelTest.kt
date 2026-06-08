package com.yourcompany.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import com.yourcompany.recipecomposeapp.data.Constants
import fixtures.RecipeTestFixtures
import kotlin.String
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewModelTest {
    private val repository = mockk<RecipesRepository>()
    private lateinit var viewModel: RecipesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loads recipes for category`() = runTest {
        val recipes = RecipeTestFixtures.createRecipeDtoList(3)

        every { repository.getRecipesByCategories(1) } returns flowOf(recipes)

        viewModel = createViewModel(
            categoryId = 1,
            categoryTitle = "Завтраки",
            categoryImageUrl = "image.jpg"
        )

        advanceUntilIdle()

        assertEquals(3, viewModel.uiState.value.recipes.size)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `state reflects category title from savedState`() = runTest {
        every { repository.getRecipesByCategories(1) } returns flowOf(emptyList())

        viewModel = createViewModel(categoryTitle = "Завтраки")

        advanceUntilIdle()

        assertEquals("Завтраки", viewModel.uiState.value.categoryTitle)
    }

    @Test
    fun `shows error when repository throws`() = runTest {
        every { repository.getRecipesByCategories(1) } returns flow { throw IOException("Network error") }

        viewModel = createViewModel()

        advanceUntilIdle()

        assertEquals("Network error", viewModel.uiState.value.isError)
    }

    private fun createViewModel(
        categoryId: Int = 1,
        categoryTitle: String = "Завтраки",
        categoryImageUrl: String = "image.jpg"
    ) = RecipesViewModel(
        savedStateHandle = SavedStateHandle(
            mapOf(
                Constants.KEY_CATEGORY_ID to categoryId,
                Constants.KEY_CATEGORY_TITLE to categoryTitle,
                Constants.KEY_CATEGORY_IMAGE_URL to categoryImageUrl
            )
        ),
        repository = repository
    )
}
