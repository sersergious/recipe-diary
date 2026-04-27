package com.ingridientsinc.recipe.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
        )
    ]
)
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_id") val recipeId: Int = 0,
    val name: String,
    @ColumnInfo(name = "category_id") val categoryId: Int = 0,
    // <SK> - added isFavorite column to support DB-backed favorites
    // may need to change to integer since SQLite does not have native Boolean
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
)