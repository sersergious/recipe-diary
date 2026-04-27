package com.ingridientsinc.recipe.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.ingridientsinc.recipe.data.CategoryWithRecipe
import com.ingridientsinc.recipe.data.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesWithRecipe(): Flow<List<CategoryWithRecipe>>

    @Query("SELECT * FROM categories WHERE category_id = :id")
    fun getCategoryById(id: Int): Flow<Category?>

}
