package com.kuzmins2.recipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuzmins2.recipes.model.Recipe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import kotlin.collections.find
import kotlin.text.equals

class RecipeViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _saveEvent = MutableSharedFlow<Unit>()
    val saveEvent: SharedFlow<Unit> = _saveEvent.asSharedFlow()
    private val recipeRepo = RecipeRepository()

    fun addRecipe(name: String, category: String, ingredients: List<String>, instructions: List<String>) {
        recipeRepo.addRecipe( Recipe(
            id = System.currentTimeMillis().toInt(),
            name = name,
            category = category,
            ingredients = ingredients,
            instructions = instructions
        ))
        _recipes.value = recipeRepo.getAllRecipes()
        viewModelScope.launch { _saveEvent.emit(Unit)}
    }

    fun getAllRecipes(): List<Recipe> {
        return recipeRepo.getAllRecipes()
    }

    fun getRecipeById(id: Int): Recipe? {
        return recipeRepo.getRecipeById(id)
    }

    fun getRecipesByCategory(category: String): List<Recipe> {
        return recipeRepo.getRecipesByCategory(category)
    }
}

class RecipeRepository {
    // Private immutable list
    private var recipes: List<Recipe> = emptyList()
    fun addRecipe(recipe: Recipe) {
        recipes = recipes + recipe
    }

    fun getAllRecipes(): List<Recipe> {
        return recipes
    }

    fun getRecipeById(id: Int): Recipe? {
        return recipes.find { it.id == id }
    }

    fun getRecipesByCategory(category: String): List<Recipe> {
        return recipes
            .filter { it.category.equals(category, ignoreCase = true) }
            .sortedBy { it.name }
    }
}

