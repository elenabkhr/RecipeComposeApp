package com.yourcompany.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.yourcompany.recipecomposeapp.data.Constants
import fixtures.RecipeTestFixtures
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

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(3, state.recipes.size)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state reflects category title from savedState`() = runTest {
        every { repository.getRecipesByCategories(1) } returns flowOf(emptyList())

        viewModel = createViewModel(categoryTitle = "Завтраки")

        viewModel.uiState.test {
            assertEquals("Завтраки", awaitItem().categoryTitle)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows error when repository throws`() = runTest {
        every { repository.getRecipesByCategories(1) } returns flow { throw IOException("Network error") }

        viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals("Network error", awaitItem().isError)
            cancelAndIgnoreRemainingEvents()
        }
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
