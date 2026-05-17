package com.yourcompany.recipecomposeapp.data.database.converter

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(string: String): List<String> {
        return if (string.isEmpty()) emptyList()
        else string.split("|||")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(separator = "|||")
    }
}