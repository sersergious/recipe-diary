package com.ingridientsinc.recipe.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ingridientsinc.recipe.data.entities.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {


    @Query("SELECT * FROM recipe WHERE recipe_id = :recipeId")
    fun getRecipeById(recipeId: Int): Flow<Recipe?>

    @Insert
    suspend fun insertRecipe(recipe: Recipe): Long


    // <SK> - added setFavorite to toggle isFavorite in-place without a full entity update
    @Query("UPDATE recipe SET is_favorite = :isFavorite WHERE recipe_id = :recipeId")
    suspend fun setFavorite(recipeId: Int, isFavorite: Boolean)

    // <SK> - added getFavoriteRecipes to drive the Favorites screen from the DB
    @Query("SELECT * FROM recipe WHERE is_favorite = 1")
    fun getFavoriteRecipes(): Flow<List<Recipe>>
}
