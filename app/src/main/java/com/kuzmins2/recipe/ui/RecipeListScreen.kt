package com.kuzmins2.recipe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuzmins2.recipe.model.Recipe
import com.kuzmins2.recipe.viewmodel.RecipeViewModel


@Composable
fun RecipeListScreen(
    viewModel: RecipeViewModel,
    onRecipeClick: (Int) -> Unit
) {
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()

    val categoryOrder = listOf("Breakfast", "Lunch", "Dinner", "Dessert")

    val grouped = recipes
        .sortedBy { it.name }
        .groupBy { it.category }
        .toSortedMap(compareBy { categoryOrder.indexOf(it) })

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 30.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        grouped.forEach { (category, recipesInCategory) ->
            item {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(recipesInCategory) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }
        }
    }
}
@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = recipe.category,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}