package com.yourcompany.recipecomposeapp.data.database.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.yourcompany.recipecomposeapp.data.database.RecipesDatabase
import com.yourcompany.recipecomposeapp.data.database.dao.CategoryDao
import com.yourcompany.recipecomposeapp.data.database.entity.CategoryEntity
import com.yourcompany.recipecomposeapp.data.model.CategoryDto
import com.yourcompany.recipecomposeapp.data.network.api.RecipesApiService
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesRepositoryIntegrationTest {

    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private var apiService = mockk<RecipesApiService>()

    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        categoryDao = database.categoryDao()
        repository = RecipesRepositoryImpl(apiService, database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun savesDataToCacheAfterSuccessfulApiCall() = runTest {
        coEvery { apiService.getCategories() } returns listOf(
            CategoryDto(
                id = 1,
                title = "Завтраки",
                description = "Лёгкие",
                imageUrl = "breakfast.jpg"
            )
        )

        repository.getCategories().test {
            awaitItem()
            val loaded = awaitItem()
            assertEquals("Завтраки", loaded.first().title)
            cancelAndIgnoreRemainingEvents()
        }

        val cached = categoryDao.getAllCategories().first()
        assertEquals(1, cached.size)
        assertEquals("Завтраки", cached[0].name)
    }

    @Test
    fun returnsCachedDataWhenApiFails() = runTest {
        categoryDao.insertCategories(
            listOf(
                CategoryEntity(
                    id = 1,
                    name = "Завтраки",
                    description = "Лёгкие",
                    imageUrl = "breakfast.jpg"
                )
            )
        )

        coEvery { apiService.getCategories() } throws IOException("Network error")

        val loaded = repository.getCategories().first()

        assertEquals(1, loaded.size)
        assertEquals("Завтраки", loaded[0].title)
    }
}