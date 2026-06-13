package com.yourcompany.recipecomposeapp.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yourcompany.recipecomposeapp.data.database.RecipesDatabase
import com.yourcompany.recipecomposeapp.data.database.entity.CategoryEntity
import com.yourcompany.recipecomposeapp.data.database.entity.RecipeEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesDaoTest {
    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var recipeDao: RecipeDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        categoryDao = database.categoryDao()
        recipeDao = database.recipeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertsAndRetrievesCategories() = runTest {
        val categories = listOf(
            CategoryEntity(id = 1, name = "Завтраки", description = "Лёгкие", imageUrl = ""),
            CategoryEntity(id = 2, name = "Обеды", description = "Основные", imageUrl = "")
        )

        categoryDao.insertCategories(categories)
        val retrieved = categoryDao.getAllCategories().first()

        assertEquals(2, retrieved.size)
    }

    @Test
    fun insertReplacesDuplicateCategory() = runTest {
        val categories = listOf(
            CategoryEntity(id = 1, name = "Завтраки", description = "Лёгкие", imageUrl = ""),
            CategoryEntity(id = 1, name = "Обеды", description = "Основные", imageUrl = "")
        )

        categoryDao.insertCategories(categories)
        val retrieved = categoryDao.getAllCategories().first()

        assertEquals(1, retrieved.size)
    }

    @Test
    fun getRecipesByCategoryReturnsCorrectItems() = runTest {

        val categories = listOf(
            CategoryEntity(id = 1, name = "Завтраки", description = "Лёгкие", imageUrl = ""),
            CategoryEntity(id = 2, name = "Обеды", description = "Основные", imageUrl = "")
        )
        categoryDao.insertCategories(categories)

        val recipes = listOf(
            RecipeEntity(
                id = 1,
                title = "Сырники",
                categoryId = 1,
                imageUrl = "",
                ingredients = "",
                method = "",
            ),
            RecipeEntity(
                id = 2,
                title = "Пицца",
                categoryId = 2,
                imageUrl = "",
                ingredients = "",
                method = "",
            ),
        )

        recipeDao.insertRecipes(recipes)
        val retrieved = recipeDao.getRecipesByCategory(1).first()

        assertEquals(1, retrieved.size)
        assertTrue(retrieved.all { it.categoryId == 1 })
    }

    @Test
    fun emptyDatabaseReturnsEmptyList() = runTest {
        val retrieved = categoryDao.getAllCategories().first()
        assertTrue(retrieved.isEmpty())
    }
}