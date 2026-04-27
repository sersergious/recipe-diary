package com.ingridientsinc.recipe.repository

import com.ingridientsinc.recipe.dao.CategoryDao
import com.ingridientsinc.recipe.dao.IngredientDao
import com.ingridientsinc.recipe.dao.InstructionDao
import com.ingridientsinc.recipe.dao.RecipeDao
import com.ingridientsinc.recipe.data.CategoryWithRecipe
import com.ingridientsinc.recipe.data.entities.Category
import com.ingridientsinc.recipe.data.entities.Ingredient
import com.ingridientsinc.recipe.data.entities.Instruction
import com.ingridientsinc.recipe.data.entities.Recipe
import kotlinx.coroutines.flow.Flow

data class IngredientInput(val name: String, val quantity: Float, val unit: String)

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val instructionDao: InstructionDao,
    private val categoryDao: CategoryDao
) {
    fun getAllCategoriesWithRecipes(): Flow<List<CategoryWithRecipe>> =
        categoryDao.getCategoriesWithRecipe()

    fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAllCategories()

    fun getRecipeById(id: Int): Flow<Recipe?> =
        recipeDao.getRecipeById(id)

    fun getCategoryById(id: Int): Flow<Category?> =
        categoryDao.getCategoryById(id)

    fun getIngredientsByRecipe(id: Int): Flow<List<Ingredient>> =
        ingredientDao.getIngredientsByRecipe(id)

    fun getInstructionsByRecipe(id: Int): Flow<List<Instruction>> =
        instructionDao.getInstructionsByRecipe(id)

    suspend fun insertFullRecipe(
        name: String,
        categoryId: Int,
        ingredients: List<IngredientInput>,
        instructions: List<String>
    ) {
        val recipeId = recipeDao.insertRecipe(Recipe(name = name, categoryId = categoryId)).toInt()
        ingredients.forEach { ing ->
            ingredientDao.insertIngredient(
                Ingredient(recipeId = recipeId, name = ing.name, quantity = ing.quantity, unit = ing.unit)
            )
        }
        instructions.forEachIndexed { index, desc ->
            instructionDao.insertInstruction(
                Instruction(recipeId = recipeId, stepNumber = index + 1, description = desc)
            )
        }
    }
}
