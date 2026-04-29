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
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
    private val okHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        threadPool.execute {
            val request: Request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            try {
                okHttpClient.newCall(request).execute().use { response ->
                    val categoriesBody = response.body.string()
                    val categories = Json.decodeFromString<List<CategoryDto>>(categoriesBody)

                    Log.i("!!!", "Количество категорий: ${categories.size}")
                    Log.i("Pool", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

                    categories.forEach { category ->
                        threadPool.execute {
                            val request: Request = Request.Builder()
                                .url("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
                                .build()

                            try {
                                okHttpClient.newCall(request).execute().use { response ->
                                    val recipesBody = response.body.string()
                                    val recipes =
                                        Json.decodeFromString<List<RecipeDto>>(recipesBody)

                                    Log.i(
                                        "Pool",
                                        "Выполняю запрос на потоке: ${Thread.currentThread().name}"
                                    )
                                    Log.i(
                                        "!!!",
                                        "Название категории: ${category.title}, Количество рецептов: ${recipes.size}"
                                    )

                                }
                            } catch (e: Exception) {
                                Log.e(
                                    "!!!",
                                    "Ошибка загрузки рецептов для категории: ${category.title}",
                                    e
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
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