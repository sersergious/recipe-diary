
I have an Android app built with Kotlin and Room (no Hilt). I need help updating my repository and connecting it to my existing codebase. Here is the context:

**Current state:**
- I have a `RecipeRepository` class that uses an in-memory list — it is commented out and needs to be replaced with a Room implementation
- I have a standalone `Recipe` data class that needs to be removed since it is replaced by the Room entity
- I have a ViewModel that currently uses the old repository and needs to be updated

**Database setup:**
- 4 entity classes: `Category`, `Recipe`, `Ingredient`, `Instruction`
- 4 DAOs: `CategoryDao`, `RecipeDao`, `IngredientDao`, `InstructionDao`
- A `RecipeDatabase` singleton class
- Relationship classes: `CategoryWithRecipes`, `RecipeWithIngredients`, `RecipeWithDetails`
- All read operations return `Flow`, all write operations are `suspend` functions

**What I need done:**
1. Replace the commented out `RecipeRepository` with a Room-based implementation using `RecipeDao`, `IngredientDao`, and `InstructionDao`
2. Add an `insertFullRecipe(recipe, ingredients, instructions)` function that inserts into all three tables in sequence using the returned recipe ID
3. Remove the standalone `Recipe` data class and replace all references with the Room entity
4. Update the ViewModel to work with the new repository using `Flow` and coroutines
5. Update any UI code that was observing the old ViewModel to work with the new `Flow` based state

Please review my existing code first before making any changes and flag anything that looks inconsistent.
Address any bugs you found and specifiede in CLUADE.md