package com.yourcompany.recipecomposeapp.di

interface Factory<T> {
    fun create(): T
}