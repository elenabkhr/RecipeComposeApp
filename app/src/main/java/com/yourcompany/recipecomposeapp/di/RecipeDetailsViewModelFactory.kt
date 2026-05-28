package com.yourcompany.recipecomposeapp.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.yourcompany.recipecomposeapp.data.repository.RecipesRepository
import com.yourcompany.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel

class RecipeDetailsViewModelFactory(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository,
) : Factory<RecipeDetailsViewModel> {

    override fun create(): RecipeDetailsViewModel {
        return RecipeDetailsViewModel(application, savedStateHandle, repository)
    }
}