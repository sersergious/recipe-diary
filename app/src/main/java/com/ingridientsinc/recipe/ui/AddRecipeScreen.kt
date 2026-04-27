package com.ingridientsinc.recipe.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import com.ingridientsinc.recipe.viewmodel.RecipeViewModel


// For managing the UI State of the AddRecipeScreen
// and monitoring all the input, I need a data class.
// This removes some of the redundancy and minimizes
// the number of lines of code.
data class AddRecipeState(
    val recipeName: String = "",
    val selectedCategory: String = "Breakfast",
    val ingredientInput: String = "",
    val instructionInput: String = "",
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val expanded: Boolean = false,
    val submitted: Boolean = false,
    val showDialog: Boolean = false
)

@Composable
fun AddRecipeScreen(viewModel: RecipeViewModel) {
    var state by remember { mutableStateOf(AddRecipeState()) }
    val isFormValid = state.recipeName.isNotBlank()
            && state.ingredients.isNotEmpty()
            && state.instructions.isNotEmpty()

    LaunchedEffect(Unit) {
        viewModel.saveEvent.collect {
            state = AddRecipeState(showDialog = true)
        }
    }

    if (state.showDialog) {
        SavedRecipeDialog(onDismiss = { state = state.copy(showDialog = false) })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            AddRecipeHeader()
            AddRecipeForm(
                state = state,
                isFormValid = isFormValid,
                onStateChange = { state = it },
                onSave = {
                    state = state.copy(submitted = true)
                    if (isFormValid) viewModel.addRecipe(
                        state.recipeName,
                        state.selectedCategory,
                        state.ingredients,
                        state.instructions
                    )
                }
            )
        }
    }
}

@Composable
fun AddRecipeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Restaurant,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = "New Recipe",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AddRecipeForm(
    state: AddRecipeState,
    isFormValid: Boolean,
    onStateChange: (AddRecipeState) -> Unit,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RecipeNameField(
                value = state.recipeName,
                onValueChange = { onStateChange(state.copy(recipeName = it)) },
                isError = state.submitted && state.recipeName.isBlank()
            )
            CategoryDropdown(
                selectedCategory = state.selectedCategory,
                expanded = state.expanded,
                onExpandedChange = { onStateChange(state.copy(expanded = it)) },
                onCategorySelected = {
                    onStateChange(state.copy(selectedCategory = it, expanded = false))
                }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            StringListInput(
                label = "Ingredients",
                input = state.ingredientInput,
                items = state.ingredients,
                isError = state.submitted && state.ingredients.isEmpty(),
                onInputChange = { onStateChange(state.copy(ingredientInput = it)) },
                onAddItem = {
                    if (state.ingredientInput.isNotBlank()) {
                        onStateChange(
                            state.copy(
                                ingredients = state.ingredients + state.ingredientInput.trim(),
                                ingredientInput = ""
                            )
                        )
                    }
                },
                onRemoveItem = { onStateChange(state.copy(ingredients = state.ingredients - it)) }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            StringListInput(
                label = "Instructions",
                input = state.instructionInput,
                items = state.instructions,
                isError = state.submitted && state.instructions.isEmpty(),
                onInputChange = { onStateChange(state.copy(instructionInput = it)) },
                onAddItem = {
                    if (state.instructionInput.isNotBlank()) {
                        onStateChange(
                            state.copy(
                                instructions = state.instructions + state.instructionInput.trim(),
                                instructionInput = ""
                            )
                        )
                    }
                },
                onRemoveItem = { onStateChange(state.copy(instructions = state.instructions - it)) }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SaveRecipeButton(enabled = isFormValid, onClick = onSave)
        }
    }
}

@Composable
fun SavedRecipeDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "Recipe Saved!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Your recipe has been saved successfully.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Great!")
            }
        }
    )
}

@Composable
fun RecipeNameField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Recipe Name") },
        isError = isError,
        supportingText = {
            if (isError) Text("Recipe name is required", color = MaterialTheme.colorScheme.error)
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CategoryDropdown(
    selectedCategory: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(it) }
    ) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                    }
                )
            }
        }
    }
}

@Composable
fun StringListInput(
    label: String,
    input: String,
    items: List<String>,
    isError: Boolean = false,
    onInputChange: (String) -> Unit,
    onAddItem: () -> Unit,
    onRemoveItem: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Section label with icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (label == "Ingredients") Icons.AutoMirrored.Filled.List
                else Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
                    .padding(6.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                label = { Text("Add $label") },
                isError = isError,
                supportingText = {
                    if (isError) Text(
                        "Add at least one $label",
                        color = MaterialTheme.colorScheme.error
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                ),
                modifier = Modifier.weight(1f)
            )
            FilledIconButton(
                onClick = onAddItem,
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add $label",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        items.forEachIndexed { index, item ->
            StringListItem(
                index = index + 1,
                text = item,
                onRemove = { onRemoveItem(item) }
            )
        }
    }
}

@Composable
fun StringListItem(index: Int, text: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$index.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun SaveRecipeButton(onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Icon(
            imageVector = Icons.Default.Save,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Save Recipe",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}