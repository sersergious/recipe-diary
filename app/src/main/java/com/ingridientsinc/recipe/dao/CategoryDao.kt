package com.ingridientsinc.recipe.dao

import androidx.room.Dao
import androidx.room.Query
import com.ingridientsinc.recipe.data.Categories

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): List<Categories>
}