package com.yourcompany.recipecomposeapp.features.recipes.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class RecipesComposeScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<RecipesComposeScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("recipes_screen") }
    ) {
    val loadingIndicator = child<KNode> { hasTestTag("loading_indicator") }
    val emptyState = child<KNode> { hasTestTag("empty_state") }
}