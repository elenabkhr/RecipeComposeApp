package com.yourcompany.recipecomposeapp.core.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteDataStoreManagerTest {

    private lateinit var context: Context
    private lateinit var manager: FavoriteDataStoreManager

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        manager = FavoriteDataStoreManager(context)
    }

    @After
    fun tearDown() {
        runBlocking { context.dataStore.edit { it.clear() } }
    }

    @Test
    fun addFavoriteSavesRecipeId() = runTest {
        manager.addFavorite(recipeId = 42)
        assertTrue(manager.getFavoriteIdsFlow().first().contains(42))
    }

    @Test
    fun removeFromFavoritesDeletesRecipeId() = runTest {
        manager.addFavorite(recipeId = 42)
        manager.removeFavorite(recipeId = 42)
        assertTrue(manager.getFavoriteIdsFlow().first().isEmpty())
    }

    @Test
    fun favoritesFlowEmitsUpdatesReactively() = runTest {
        manager.getFavoriteIdsFlow().test {
            awaitItem()

            manager.addFavorite(recipeId = 42)

            val favorite = awaitItem()
            assertTrue(favorite.contains(42))
        }
    }
}
