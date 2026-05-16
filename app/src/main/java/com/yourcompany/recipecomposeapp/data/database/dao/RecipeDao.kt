package com.yourcompany.recipecomposeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.yourcompany.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes WHERE category_id = :categoryId ORDER BY title ASC")
    fun getAllRecipesByCategory(categoryId: Int): Flow<List<RecipeEntity>>
}
