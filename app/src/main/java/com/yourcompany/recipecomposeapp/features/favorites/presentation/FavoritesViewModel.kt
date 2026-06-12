package com.yourcompany.recipecomposeapp.features.favorites.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.recipecomposeapp.core.utils.FavoriteDataStoreManager
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepository
import com.yourcompany.recipecomposeapp.features.favorites.presentation.model.FavoritesUiState
import com.yourcompany.recipecomposeapp.features.recipes.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteManager: FavoriteDataStoreManager,
    private val repository: RecipesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeFavorites() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, isError = null)
            }

            favoriteManager.getFavoriteIdsFlow()
                .flatMapLatest { ids ->
                    val recipeFlows = ids.mapNotNull {
                        try {
                            repository.getRecipe(it)
                                .map { dto -> dto?.toUiModel() }

                        } catch (e: NumberFormatException) {
                            _uiState.update { currentState ->
                                currentState.copy(isLoading = false, isError = "Network error")
                            }

                            Log.e("FavoritesViewModel", "Failed to get recipes by ids", e)
                            null
                        }
                    }

                    if (recipeFlows.isEmpty()) {
                        flowOf(emptyList())
                    } else {
                        combine(recipeFlows) { recipes ->
                            recipes.filterNotNull().toList()
                        }
                    }
                }

                .collect { recipes ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            recipes = recipes,
                            isLoading = false,
                            isInitialized = true,
                        )
                    }
                }
        }
    }
}
