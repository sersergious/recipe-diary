package com.ingridientsinc.recipe.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ingridientsinc.recipe.data.entities.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients WHERE recipe_id = :recipeId")
    fun getIngredientsByRecipe(recipeId: Int): Flow<List<Ingredient>>

    @Insert
    suspend fun insertIngredient(ingredient: Ingredient)
}
