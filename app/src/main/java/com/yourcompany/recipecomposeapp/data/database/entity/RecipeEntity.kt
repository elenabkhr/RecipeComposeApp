package com.yourcompany.recipecomposeapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipes",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["category_id"]),
        Index(value = ["title"])
    ],
)
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    val imageUrl: String,
    val ingredients: String,
    val method: String,
)
