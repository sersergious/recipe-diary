package com.ingridientsinc.recipe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "instructions",
    foreignKeys = [
        ForeignKey(
            entity = Recipes::class,
            parentColumns = ["id"],
            childColumns = ["recipe_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Instructions(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = "recipe_id")
    val recipeId: Int,
    @ColumnInfo(name = "step_number")
    val stepNumber: Int,
    val description: String)
