package com.ingridientsinc.recipe.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "instructions",
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["recipe_id"],
            childColumns = ["recipe_id"],
        )
    ]
)
data class Instruction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "instruction_id") val instructionId: Int = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: Int = 0,
    @ColumnInfo(name = "step_number") val stepNumber: Int = 0,
    val description: String = ""
)
