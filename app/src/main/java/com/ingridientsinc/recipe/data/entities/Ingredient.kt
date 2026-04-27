package com.ingridientsinc.recipe.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["recipe_id"],
            childColumns = ["recipe_id"],
        )
    ]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ingredient_id") val ingredientId: Int = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: Int = 0,
    val name: String,
    val quantity: Float,
    val unit: String
)
