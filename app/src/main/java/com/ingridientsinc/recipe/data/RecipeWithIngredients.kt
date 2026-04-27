package com.ingridientsinc.recipe.data

import androidx.room.Embedded
import androidx.room.Relation
import com.ingridientsinc.recipe.data.entities.Ingredient
import com.ingridientsinc.recipe.data.entities.Recipe

data class RecipeWithIngredients(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "recipe_id",
        entityColumn = "recipe_id"
    )
    val ingredients: List<Ingredient>
)
