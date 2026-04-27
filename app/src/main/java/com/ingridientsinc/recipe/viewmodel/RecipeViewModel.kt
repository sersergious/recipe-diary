package com.ingridientsinc.recipe.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingridientsinc.recipe.model.Recipe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class RecipeViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _saveEvent = MutableSharedFlow<Unit>()
    val saveEvent: SharedFlow<Unit> = _saveEvent.asSharedFlow()
    private val recipeRepo = RecipeRepository()

    //<LK>: What I added to this file
    private val _favoriteIds =  MutableStateFlow<Set<Int>>(emptySet())
    val favorites: StateFlow<List<Recipe>> = _favoriteIds
        .combine(_recipes) { ids, all -> all.filter {it.id in ids}}
        .stateIn(viewModelScope, SharingStarted.Eagerly,emptyList())

    fun toggleFavorite(recipeId: Int){
        _favoriteIds.value = _favoriteIds.value.let {current ->
            if (recipeId in current) current - recipeId else current + recipeId
        }
    }

    fun isFavorite(recipeId: Int): Boolean = recipeId in _favoriteIds.value
    //<LK>: End of what I added to this file
    fun addRecipe(
        name: String,
        category: String,
        ingredients: List<String>,
        instructions: List<String>
    ) {
        recipeRepo.addRecipe(
            Recipe(
                id = System.currentTimeMillis().toInt(),
                name = name,
                category = category,
                ingredients = ingredients,
                instructions = instructions
            )
        )
        _recipes.value = getAllRecipes()
        viewModelScope.launch { _saveEvent.emit(Unit) }
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

