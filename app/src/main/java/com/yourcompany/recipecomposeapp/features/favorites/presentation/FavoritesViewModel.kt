package com.yourcompany.recipecomposeapp.features.favorites.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepository
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    application: Application,
    private val repository: RecipesRepository,
) : AndroidViewModel(application) {
    private val favoriteManager = FavoriteDataStoreManager(application)

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    fun observeFavorites() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, isError = null)
            }

            favoriteManager.getFavoriteIdsFlow()
                .map { ids ->
                    ids.mapNotNull {
                        try {
                            repository.getRecipe(it.toInt())
                        } catch (e: NumberFormatException) {
                            Log.e("FavoritesViewModel", "Failed to get recipes by ids", e)
                            null
                        }
                    }
                }
                .collect { recipes ->
                    _uiState.update {
                        it.copy(
                            recipes = recipes.map { it.toUiModel() },
                            isLoading = false
                        )
                    }
                }
        }
    }
}
