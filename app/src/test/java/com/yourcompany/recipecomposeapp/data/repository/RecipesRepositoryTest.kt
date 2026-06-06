package com.yourcompany.recipecomposeapp.data.repository

import app.cash.turbine.test
import com.yourcompany.recipecomposeapp.data.database.RecipesDatabase
import com.yourcompany.recipecomposeapp.data.database.dao.CategoryDao
import com.yourcompany.recipecomposeapp.data.database.dao.RecipeDao
import com.yourcompany.recipecomposeapp.data.database.entity.CategoryEntity
import com.yourcompany.recipecomposeapp.data.database.entity.RecipeEntity
import com.yourcompany.recipecomposeapp.data.network.api.RecipesApiService
import fixtures.RecipeTestFixtures
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecipesRepositoryTest {
    private val apiService = mockk<RecipesApiService>()
    private val database = mockk<RecipesDatabase>(relaxed = true)
    private val categoryDao = mockk<CategoryDao>()
    private val recipeDao = mockk<RecipeDao>()

    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setup() {
        every { database.categoryDao() } returns categoryDao
        every { database.recipeDao() } returns recipeDao
        repository = RecipesRepositoryImpl(apiService, database)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCategories emits categories from database`() = runTest {
        every { categoryDao.getAllCategories() } returns flowOf(
            listOf(
                CategoryEntity(
                    id = 1,
                    name = "Завтраки",
                    description = "Утренние блюда",
                    imageUrl = "breakfast.jpg"
                )
            )
        )
        coEvery { apiService.getCategories() } returns emptyList()
        coEvery { categoryDao.insertCategories(any()) } just Runs

        repository.getCategories().test {
            val categories = awaitItem()
            assertEquals(1, categories.size)
            assertEquals("Завтраки", categories[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getCategories still emits data when api throws exception`() = runTest {
        every { categoryDao.getAllCategories() } returns flowOf(
            listOf(
                CategoryEntity(
                    id = 1,
                    name = "Завтраки",
                    description = "Утренние блюда",
                    imageUrl = "breakfast.jpg"
                )
            )
        )

        coEvery { apiService.getCategories() } throws IOException()

        repository.getCategories().test {
            val categories = awaitItem()
            assertEquals(1, categories.size)
            assertEquals("Завтраки", categories[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRecipesByCategory returns flow filtered by categoryId`() = runTest {
        every { recipeDao.getRecipesByCategory(1) } returns flowOf(
            listOf(
                RecipeEntity(
                    id = 1,
                    title = "Паста",
                    categoryId = 1,
                    imageUrl = "pasta.jpg",
                    ingredients = """[
                    {"quantity":"200","unitOfMeasure":"g","description":"Паста"}
                    ]""",
                    method = "Отварить|||Смешать",
                )
            )
        )

        coEvery { apiService.getRecipesByCategory(1) } returns emptyList()
        coEvery { recipeDao.insertRecipes(any()) } just Runs

        repository.getRecipesByCategories(1).test {
            val recipes = awaitItem()
            assertEquals(1, recipes.size)
            assertEquals("Паста", recipes[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRecipesByCategory still emits data when api throws exception`() = runTest {
        every { recipeDao.getRecipesByCategory(1) } returns flowOf(
            listOf(
                RecipeEntity(
                    id = 1,
                    title = "Паста",
                    categoryId = 1,
                    imageUrl = "pasta.jpg",
                    ingredients = """[
                    {"quantity":"200","unitOfMeasure":"g","description":"Паста"}
                    ]""",
                    method = "Отварить|||Смешать",
                )
            )
        )

        coEvery { apiService.getRecipesByCategory(1) } throws IOException()

        repository.getRecipesByCategories(1).test {
            val recipes = awaitItem()
            assertEquals(1, recipes.size)
            assertEquals("Паста", recipes[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRecipe returns flow by recipeId`() = runTest {
        every { recipeDao.getRecipeById(1) } returns flowOf(
            RecipeEntity(
                id = 1,
                title = "Паста",
                categoryId = 1,
                imageUrl = "pasta.jpg",
                ingredients = """[
                    {"quantity":"200","unitOfMeasure":"g","description":"Паста"}
                    ]""",
                method = "Отварить|||Смешать",
            )
        )

        coEvery { apiService.getRecipe(1) } returns RecipeTestFixtures.createRecipeDto()
        coEvery { recipeDao.insertRecipe(any()) } just Runs

        repository.getRecipe(1).test {
            val recipe = awaitItem()
            assertNotNull(recipe)
            assertEquals("Паста", recipe?.title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRecipe still emits data when api throws exception`() = runTest {
        every { recipeDao.getRecipeById(1) } returns flowOf(
            RecipeEntity(
                id = 1,
                title = "Паста",
                categoryId = 1,
                imageUrl = "pasta.jpg",
                ingredients = """[
                    {"quantity":"200","unitOfMeasure":"g","description":"Паста"}
                    ]""",
                method = "Отварить|||Смешать",
            )
        )

        coEvery { apiService.getRecipe(1) } throws IOException()

        repository.getRecipe(1).test {
            val recipes = awaitItem()
            assertEquals("Паста", recipes?.title)
            cancelAndIgnoreRemainingEvents()
        }
    }
}