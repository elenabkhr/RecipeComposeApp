package com.yourcompany.recipecomposeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.yourcompany.recipecomposeapp.data.model.CategoryDto
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        threadPool.execute {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val categoriesConnection = url.openConnection() as HttpURLConnection

            try {
                categoriesConnection.connect()

                val jsonCategories =
                    categoriesConnection.inputStream.bufferedReader().use { it.readText() }
                val listCategory = Json.decodeFromString<List<CategoryDto>>(jsonCategories)

                Log.i("!!!", "Количество категорий: ${listCategory.size}")
                Log.i("Pool", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

                listCategory.forEach { category ->
                    threadPool.execute {
                        val recipesUrl =
                            URL("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
                        val recipesConnection = recipesUrl.openConnection() as HttpURLConnection
                        try {
                            recipesConnection.connect()

                            val jsonRecipes =
                                recipesConnection.inputStream.bufferedReader().use { it.readText() }
                            val recipes = Json.decodeFromString<List<RecipeDto>>(jsonRecipes)

                            Log.i(
                                "Pool",
                                "Выполняю запрос на потоке: ${Thread.currentThread().name}"
                            )
                            Log.i(
                                "!!!",
                                "Название категории: ${category.title}, Количество рецептов: ${recipes.size}"
                            )

                        } catch (e: Exception) {
                            Log.e(
                                "!!!",
                                "Ошибка загрузки рецептов для категории: ${category.title}",
                                e
                            )
                        } finally {
                            recipesConnection.disconnect()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
            } finally {
                categoriesConnection.disconnect()
            }
        }

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        intent?.data?.let { _ ->
            deepLinkIntent = intent
        }

        enableEdgeToEdge()
        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { _ ->
            deepLinkIntent = intent
        }
        setIntent(intent)
    }
}