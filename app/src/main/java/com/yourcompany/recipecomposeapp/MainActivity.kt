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
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yourcompany.recipecomposeapp.core.network.NetworkConfig
import com.yourcompany.recipecomposeapp.core.network.api.RecipesApiService
import com.yourcompany.recipecomposeapp.data.model.CategoryDto
import com.yourcompany.recipecomposeapp.data.model.RecipeDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val apiService: RecipesApiService = retrofit.create(RecipesApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thread {
            try {
                val categoriesCall: Call<List<CategoryDto>> = apiService.getCategories()
                val categoriesResponse: Response<List<CategoryDto>> = categoriesCall.execute()
                val categories = categoriesResponse.body() ?: emptyList()

                Log.i("!!!", "Количество категорий: ${categories.size}")
                Log.i("Pool", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

                categories.forEach { category ->
                    thread {
                        try {
                            val recipesCall: Call<List<RecipeDto>> =
                                apiService.getRecipesByCategory(category.id)
                            val recipesResponse: Response<List<RecipeDto>> = recipesCall.execute()
                            val recipes = recipesResponse.body() ?: emptyList()

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
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { _ ->
            deepLinkIntent = intent
        }
        setIntent(intent)
    }
}