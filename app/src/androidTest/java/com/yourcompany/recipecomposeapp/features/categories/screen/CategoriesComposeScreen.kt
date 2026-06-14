package com.yourcompany.recipecomposeapp.features.categories.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CategoriesComposeScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CategoriesComposeScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("categories_screen") }
    ) {
    val loadingIndicator = child<KNode> { hasTestTag("loading_indicator") }
    val categoriesGrid = child<KNode> { hasTestTag("categories_grid") }
    val categoryItem = child<KNode> { hasTestTag("category_item") }
}