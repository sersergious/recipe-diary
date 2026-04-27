package com.ingridientsinc.recipe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ingridientsinc.recipe.RecipeApplication
import com.ingridientsinc.recipe.data.CategoryWithRecipe
import com.ingridientsinc.recipe.data.entities.Category
import com.ingridientsinc.recipe.data.entities.Ingredient
import com.ingridientsinc.recipe.data.entities.Instruction
import com.ingridientsinc.recipe.data.entities.Recipe
import com.ingridientsinc.recipe.repository.IngredientInput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class RecipeFull(
    val recipe: Recipe,
    val category: Category?,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>
)

sealed class UiEvent {
    data object RecipeSaved : UiEvent()
    data class Error(val message: String) : UiEvent()
}

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as RecipeApplication).recipeRepository

    // Single source of truth for list screen
    val categoriesWithRecipes: StateFlow<List<CategoryWithRecipe>> =
        repository.getAllCategoriesWithRecipes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Derived from SSOT — no extra stateIn; UI supplies initial value when collecting
    val categories: Flow<List<Category>> = categoriesWithRecipes
        .map { list -> list.map { it.category } }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    // Detail screen — all related state consolidated into one StateFlow
    private val _selectedRecipeId = MutableStateFlow<Int?>(null)

    val selectedRecipeFull: StateFlow<RecipeFull?> = _selectedRecipeId
        .flatMapLatest { id ->
            if (id == null) return@flatMapLatest flowOf(null)
            combine(
                repository.getRecipeById(id),
                repository.getIngredientsByRecipe(id),
                repository.getInstructionsByRecipe(id)
            ) { recipe, ingredients, instructions ->
                Triple(recipe, ingredients, instructions)
            }.flatMapLatest { (recipe, ingredients, instructions) ->
                if (recipe == null) return@flatMapLatest flowOf(null)
                repository.getCategoryById(recipe.categoryId)
                    .map { category ->
                        RecipeFull(
                            recipe = recipe,
                            category = category,
                            ingredients = ingredients,
                            instructions = instructions
                        )
                    }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun loadRecipe(id: Int) {
        _selectedRecipeId.value = id
    }

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
        categoryId: Int,
        ingredients: List<IngredientInput>,
        instructions: List<String>
    ) {
        viewModelScope.launch {
            try {
                repository.insertFullRecipe(name, categoryId, ingredients, instructions)
                _uiEvent.emit(UiEvent.RecipeSaved)
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.Error(e.message ?: "Failed to save recipe"))
            }
        }
    }
}
