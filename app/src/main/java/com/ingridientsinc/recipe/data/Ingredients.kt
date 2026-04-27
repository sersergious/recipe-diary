package com.ingridientsinc.recipe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = Recipes::class,
            parentColumns = ["id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Ingredients(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Int = 0,
    val name: String,
    val quantity: Float,
    val unit: String
)
