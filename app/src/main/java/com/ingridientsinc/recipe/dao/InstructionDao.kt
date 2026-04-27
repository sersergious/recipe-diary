package com.ingridientsinc.recipe.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ingridientsinc.recipe.data.entities.Instruction
import kotlinx.coroutines.flow.Flow

@Dao
interface InstructionDao {
    @Query("SELECT * FROM instructions WHERE recipe_id = :recipeId ORDER BY step_number ASC")
    fun getInstructionsByRecipe(recipeId: Int): Flow<List<Instruction>>

    @Insert
    suspend fun insertInstruction(instruction: Instruction)

    @Update
    suspend fun updateInstruction(instruction: Instruction)

    @Delete
    suspend fun deleteInstruction(instruction: Instruction)
}
