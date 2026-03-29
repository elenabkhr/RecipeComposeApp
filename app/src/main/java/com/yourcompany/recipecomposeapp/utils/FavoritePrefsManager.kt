package com.yourcompany.recipecomposeapp.utils

import android.content.Context
import androidx.core.content.edit
import com.yourcompany.recipecomposeapp.data.KEY_FAVORITES
import com.yourcompany.recipecomposeapp.data.KEY_PREFS

class FavoritesPrefsManager(context: Context) {
    val sharedPreferences = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)

    fun isFavorite(recipeId: Int): Boolean {
        val favoriteRecipeIds =
            sharedPreferences.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet<String>()
        return favoriteRecipeIds.contains(recipeId.toString())
    }

    fun addToFavorites(recipeId: Int) {
        val currentFavorites = sharedPreferences.getStringSet(KEY_FAVORITES, emptySet())
        val updatedFavorites = currentFavorites?.toMutableSet() ?: mutableSetOf()
        updatedFavorites.add(recipeId.toString())
        sharedPreferences.edit {
            putStringSet(KEY_FAVORITES, updatedFavorites)
        }
    }

    fun removeFromFavorites(recipeId: Int) {
        val currentFavorites = sharedPreferences.getStringSet(KEY_FAVORITES, emptySet())
        val updatedFavorites = currentFavorites?.toMutableSet() ?: mutableSetOf()
        updatedFavorites.remove(recipeId.toString())
        sharedPreferences.edit {
            putStringSet(KEY_FAVORITES, updatedFavorites)
        }
    }

    fun getAllFavorites(): Set<String> {
        val favoriteRecipeId =
            sharedPreferences.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet<String>()
        return favoriteRecipeId
    }
}