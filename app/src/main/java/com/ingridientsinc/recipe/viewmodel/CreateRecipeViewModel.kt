package com.ingridientsinc.recipe.viewmodel

import androidx.lifecycle.ViewModel
import com.ingridientsinc.recipe.repository.IngredientInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//<LK>: UI state moved out of AddRecipeScreen so it can be shared
//across the 3 nested-graph step screen via the scoped ViewModel

// <SK> - split ingredientInput into three fields for structured entry; changed ingredients to List<IngredientInput>
data class AddRecipeState(
    val recipeName: String = "",
    val selectedCategory: String = "Breakfast",
    val ingredientName: String = "",
    val ingredientQuantity: String = "",
    val ingredientUnit: String = "cup",
    val instructionInput: String = "",
    val ingredients: List<IngredientInput> = emptyList(),
    val instructions: List<String> = emptyList(),
    val expanded: Boolean = false,
    val submitted: Boolean = false
    )

//<LK>: Graph-scoped via viewModel in NavGraph.kt. One instance per
// CreateGraph entry. This is cleared on exit
class CreateRecipeViewModel: ViewModel() {
    private val _state = MutableStateFlow(AddRecipeState())
    val state: StateFlow<AddRecipeState> = _state.asStateFlow()

    fun update(transform: (AddRecipeState) -> AddRecipeState) {
        _state.value = transform(_state.value)
    }

    // <SK> - updated to parse qauntity + unit + name into an IngredientInput; clears all three input fields on add
    fun addIngredient() {
        val currentState = _state.value
        if (currentState.ingredientName.isNotBlank()) {
            val qty = currentState.ingredientQuantity.toFloatOrNull() ?: 0f
            _state.value = currentState.copy(
                ingredients = currentState.ingredients + IngredientInput(
                    name = currentState.ingredientName.trim(),
                    quantity = qty,
                    unit = currentState.ingredientUnit
                ),
                ingredientName = "",
                ingredientQuantity = ""
            )
        }
    }

    // <SK> - changed parameter type String → IngredientInput to match new ingredients list type
    fun removeIngredient(item: IngredientInput) {
        _state.value = _state.value.let {
            currentState -> currentState.copy(ingredients = currentState.ingredients - item)
        }
    }

    fun addInstruction(){
        val currentState = _state.value
        if (currentState.instructionInput.isNotBlank()){
            _state.value = currentState.copy(
                instructions = currentState.instructions + currentState.instructionInput.trim(),
                instructionInput = ""
            )
        }
    }

    fun removeInstruction(item: String){
        _state.value = _state.value.let {
            currentState -> currentState.copy(instructions = currentState.instructions - item)
        }
    }

    fun markSubmitted(){
        _state.value = _state.value.copy(submitted = true)
    }

    fun isValid(): Boolean = _state.value.let{
        it.recipeName.isNotBlank() &&
                it.ingredients.isNotEmpty() &&
                it.instructions.isNotEmpty()
    }
}
