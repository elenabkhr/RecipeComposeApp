package com.yourcompany.recipecomposeapp.features.categories.presentation

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
import kotlinx.coroutines.test.advanceUntilIdle
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

        advanceUntilIdle()

        assertEquals(3, viewModel.uiState.value.categories.size)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `shows empty list when repository returns no data`() = runTest {
        every { repository.getCategories() } returns flowOf(emptyList())
        viewModel = CategoriesViewModel(repository)

        assertTrue(viewModel.uiState.value.categories.isEmpty())
        assertNull(viewModel.uiState.value.isError)
    }

    @Test
    fun `shows error when repository throws`() = runTest {
        every { repository.getCategories() } returns flow { throw IOException("Network error") }

        viewModel = CategoriesViewModel(repository)

        advanceUntilIdle()

        assertEquals("Network error", viewModel.uiState.value.isError)
    }
}
