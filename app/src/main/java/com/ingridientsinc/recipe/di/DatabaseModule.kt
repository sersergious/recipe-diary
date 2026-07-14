package com.ingridientsinc.recipe.di

import android.content.Context
import androidx.room.Room
import com.ingridientsinc.recipe.dao.CategoryDao
import com.ingridientsinc.recipe.dao.IngredientDao
import com.ingridientsinc.recipe.dao.InstructionDao
import com.ingridientsinc.recipe.dao.RecipeDao
import com.ingridientsinc.recipe.data.RecipesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RecipesDatabase {
        return Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            RecipesDatabase.DATABASE_NAME
        )
            .addCallback(RecipesDatabase.seedCallback)
            .build()
    }

    @Provides
    fun provideRecipeDao(database: RecipesDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    fun provideIngredientDao(database: RecipesDatabase): IngredientDao {
        return database.ingredientDao()
    }

    @Provides
    fun provideInstructionDao(database: RecipesDatabase): InstructionDao {
        return database.instructionDao()
    }

    @Provides
    fun provideCategoryDao(database: RecipesDatabase): CategoryDao {
        return database.categoryDao()
    }
}
