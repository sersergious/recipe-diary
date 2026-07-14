package com.ingridientsinc.recipe.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ingridientsinc.recipe.dao.CategoryDao
import com.ingridientsinc.recipe.dao.IngredientDao
import com.ingridientsinc.recipe.dao.InstructionDao
import com.ingridientsinc.recipe.dao.RecipeDao
import com.ingridientsinc.recipe.data.entities.Category
import com.ingridientsinc.recipe.data.entities.Ingredient
import com.ingridientsinc.recipe.data.entities.Instruction
import com.ingridientsinc.recipe.data.entities.Recipe

// <SK> - reset to version 1; user will clear app data manually to get a fresh DB with the updated schema
@Database(entities = [Category::class, Ingredient::class, Instruction::class, Recipe::class], version = 1, exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun instructionDao(): InstructionDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        const val DATABASE_NAME = "recipe_database"

        val seedCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO categories (category_id, name) VALUES (1, 'Breakfast')")
                db.execSQL("INSERT INTO categories (category_id, name) VALUES (2, 'Lunch')")
                db.execSQL("INSERT INTO categories (category_id, name) VALUES (3, 'Dinner')")
                db.execSQL("INSERT INTO categories (category_id, name) VALUES (4, 'Dessert')")
            }
        }
    }
}
