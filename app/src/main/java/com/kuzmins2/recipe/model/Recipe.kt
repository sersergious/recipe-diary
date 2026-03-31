package com.kuzmins2.recipe.model

data class Recipe(
    val id: Int,
    val name: String,
    val category: String,
    val ingredients: List<String>,
    val instructions: List<String>
)