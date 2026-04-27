package com.ingridientsinc.recipe.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ingridientsinc.recipe.data.Instructions

@Dao
interface InstructionDao {
    @Query("SELECT * FROM instructions WHERE recipe_id = :recipeId ORDER BY step_number ASC")
    fun getInstructionsByRecipe(recipeId: Int): List<Instructions>

    @Insert
    fun insertInstruction(instruction: Instructions)

    @Update
    fun updateInstruction(instruction: Instructions)

    @Delete
    fun deleteInstruction(instruction: Instructions)
}