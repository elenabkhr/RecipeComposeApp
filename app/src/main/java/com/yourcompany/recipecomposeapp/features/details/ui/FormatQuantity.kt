package com.yourcompany.recipecomposeapp.features.details.ui

fun formatQuantity(quantity: Double): String {
    val quantityInt = quantity.toInt()
    val fraction = quantity - quantityInt

    val fractionString = when {
        fraction >= 0.75 -> "3/4"
        fraction >= 0.5 -> "1/2"
        fraction >= 0.25 -> "1/4"
        fraction > 0.0 -> "щепотка"
        else -> ""
    }

    return when {
        quantityInt == 0 -> fractionString
        fractionString.isEmpty() -> "$quantityInt"
        else -> "$quantityInt $fractionString"
    }
}