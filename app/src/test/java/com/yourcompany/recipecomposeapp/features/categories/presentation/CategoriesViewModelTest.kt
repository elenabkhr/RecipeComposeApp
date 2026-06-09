package com.yourcompany.recipecomposeapp.features.categories.presentation

import app.cash.turbine.test
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepository
import fixtures.CategoryTestFixtures
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
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
class CategoriesViewModelTest {
    private val repository = mockk<RecipesRepository>()
    private lateinit var viewModel: CategoriesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { repository.getCategories() } returns flowOf(emptyList())
        viewModel = CategoriesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loads categories from repository`() = runTest {
        val categories = CategoryTestFixtures.createCategoryDtoList(3)

        every { repository.getCategories() } returns flowOf(categories)

        val viewModel = CategoriesViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(3, state.categories.size)
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows empty list when repository returns no data`() = runTest {
        every { repository.getCategories() } returns flowOf(emptyList())
        viewModel = CategoriesViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.categories.isEmpty())
            assertNull(state.isError)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows error when repository throws`() = runTest {
        every { repository.getCategories() } returns flow { throw IOException("Network error") }

        viewModel = CategoriesViewModel(repository)

        viewModel.uiState.test {
            assertEquals("Network error", awaitItem().isError)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
