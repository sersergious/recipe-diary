package com.ingridientsinc.recipe

import android.app.Application
import android.util.Log
import com.ingridientsinc.recipe.data.RecipesDatabase
import com.ingridientsinc.recipe.repository.RecipeRepository

class RecipeApplication : Application() {
    lateinit var recipeRepository: RecipeRepository

    override fun onCreate() {
        super.onCreate()
        val db = RecipesDatabase.getDatabase(this)
        recipeRepository = RecipeRepository(
            db.recipeDao(),
            db.ingredientDao(),
            db.instructionDao(),
            db.categoryDao()
        )
        Log.i("ON APP START", "App Started")
    }
}
