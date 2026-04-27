package com.ingridientsinc.recipe.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ingridientsinc.recipe.data.Ingredients

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients WHERE recipe_id = :recipeId")
    fun getIngredientsByRecipe(recipeId: Int): List<Ingredients>

    @Insert
    fun insertIngredient(ingredient: Ingredients)

    @Update
    fun updateIngredient(ingredient: Ingredients)

    @Delete
    fun deleteIngredient(ingredient: Ingredients)
}