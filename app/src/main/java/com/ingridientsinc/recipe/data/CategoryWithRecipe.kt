package com.ingridientsinc.recipe.data

import androidx.room.Embedded
import androidx.room.Relation
import com.ingridientsinc.recipe.data.entities.Category
import com.ingridientsinc.recipe.data.entities.Recipe

data class CategoryWithRecipe(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "category_id"
    )
    val recipes: List<Recipe>
)
