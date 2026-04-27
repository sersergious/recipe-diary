package com.ingridientsinc.recipe.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ingridientsinc.recipe.data.RecipeWithIngredients
import com.ingridientsinc.recipe.data.RecipeWithInstructions
import com.ingridientsinc.recipe.data.entities.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE recipe_id = :recipeId")
    fun getRecipeById(recipeId: Int): Flow<Recipe?>

    @Transaction
    @Query("SELECT * FROM recipe WHERE recipe_id = :recipeId")
    fun getRecipeWithIngredients(recipeId: Int): Flow<RecipeWithIngredients>

    @Transaction
    @Query("SELECT * FROM recipe WHERE recipe_id = :recipeId")
    fun getRecipeWithInstructions(recipeId: Int): Flow<RecipeWithInstructions>

    @Query("SELECT * FROM recipe WHERE category_id = :categoryId")
    fun getRecipesByCategory(categoryId: Int): Flow<List<Recipe>>

    @Insert
    suspend fun insertRecipe(recipe: Recipe): Long

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)
}
