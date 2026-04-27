# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew test --tests "com.ingridientsinc.recipe.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Lint
./gradlew lint

# Full clean build
./gradlew clean assembleDebug
```

Room uses KSP for code generation (`ksp` plugin). After changing any `@Entity`, `@Dao`, or `@Database` class, a rebuild is required for generated code to update.

## Architecture

The app follows MVVM and is structured in these layers:

```
ui/           ← Compose screens (RecipeListScreen, AddRecipeScreen, RecipeDetailScreen)
viewmodel/    ← RecipeViewModel (StateFlow + SharedFlow for UI events)
repository/   ← RecipeRepository (currently commented out — in progress)
dao/          ← Room DAOs (CategoryDao, IngredientDao, InstructionDao, RecipeDao)
data/         ← RecipesDatabase singleton + Room relation classes
data/entities/← Room @Entity classes (Category, Recipe, Ingredient, Instruction)
model/        ← In-memory Recipe data class (used by ViewModel currently)
navigation/   ← NavGraph + Screen sealed class
```

### Active vs. In-Progress Database Layer

There are **two parallel data models** — this is the most important thing to understand:

1. **`model/Recipe.kt`** — in-memory model with `List<String>` for ingredients and instructions. Currently used by `RecipeViewModel` and all UI screens.
2. **`data/entities/`** — proper Room entities with foreign keys and separate tables for ingredients and instructions.

The `RecipeRepository` in `repository/RecipeRepository.kt` is fully **commented out**. Meanwhile, `RecipeViewModel` contains its own inline `class RecipeRepository {}` stub that does nothing (no persistence). The goal of the `database` branch is to wire the Room layer into the ViewModel, replacing the stub.

### Navigation

Three screens are registered in `NavGraph.kt`:
- `Screen.Browse` → `RecipeListScreen` (list grouped by category)
- `Screen.Add` → `AddRecipeScreen`
- `Screen.Detail` → `RecipeDetailScreen` (receives `recipeId: Int` as nav argument)

`NavigationSuiteScaffold` in `MainActivity` provides adaptive nav (bottom bar on phones, rail on tablets) for Browse and Add tabs.

### Database Schema

Four Room entities — see `README.md` for the ERD:
- `categories(category_id, name)`
- `recipe(recipe_id, name, category_id FK→categories)`
- `ingredient(ingredient_id, recipe_id FK→recipe, name, quantity, unit)`
- `instruction(instruction_id, recipe_id FK→recipe, step_number, description)`

`RecipesDatabase` is a singleton (version 8, `fallbackToDestructiveMigration = true`). When incrementing `version`, update the `@Database` annotation.

### Known Issues in Current State

- `RecipeWithIngredients` uses `entityColumn = "ingredient_id"` — this is likely wrong and should be `recipe_id` (the FK on `Ingredient` pointing back to `Recipe`).
- `RecipeWithInstructions` may have the same issue.
- The categories are hardcoded in two places: `AddRecipeScreen.kt` (`CategoryDropdown`) and `RecipeListScreen.kt` (`categoryOrder`). They must stay in sync until driven by the database.
