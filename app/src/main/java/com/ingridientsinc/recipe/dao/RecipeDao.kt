package com.ingridientsinc.recipe.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ingridientsinc.recipe.data.Recipes

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): List<Recipes>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun getRecipeById(id: Int): Recipes

    @Query("SELECT * FROM recipe WHERE category_id = :categoryId")
    fun getRecipesByCategory(categoryId: Int): List<Recipes>

    @Insert
    fun insertRecipe(recipes: Recipes)

    @Update
    fun updateRecipe(recipes: Recipes)

    @Delete
    fun deleteRecipe(recipes: Recipes)
}