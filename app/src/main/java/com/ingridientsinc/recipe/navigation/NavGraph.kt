package com.ingridientsinc.recipe.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.ingridientsinc.recipe.ui.CreateDetailsScreen
import com.ingridientsinc.recipe.ui.CreateIngredientsScreen
import com.ingridientsinc.recipe.ui.CreateStepsScreen
import com.ingridientsinc.recipe.ui.FavoritesScreen
import com.ingridientsinc.recipe.ui.RecipeDetailScreen
import com.ingridientsinc.recipe.ui.RecipeListScreen
import com.ingridientsinc.recipe.viewmodel.CreateRecipeViewModel
import com.ingridientsinc.recipe.viewmodel.RecipeViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: RecipeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Browse.route
    ) {
        composable(Screen.Browse.route) {
            RecipeListScreen(
                viewModel = viewModel,
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.Detail.createRoute(recipeId))
                }
            )
        }

        // <LK>: Favorites list - taps navigate to Detail just like Browse
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                viewModel = viewModel,
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.Detail.createRoute(recipeId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId")
            recipeId?.let {
                RecipeDetailScreen(recipeId = it, viewModel = viewModel)
            }
        }

        // <LK>: Nested graph for the 3-step Create Recipe flow.
        // All 3-step composables share ONE CreateRecipeViewModel
        // scoped to the CreateGraph back stack entry.
        navigation(
            route = Screen.CreateGraph.route,
            startDestination = Screen.CreateDetails.route
        ) {
            composable(Screen.CreateDetails.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.CreateGraph.route)
                }
                val createVm: CreateRecipeViewModel =
                    viewModel(viewModelStoreOwner = parentEntry)
                CreateDetailsScreen(
                    createVm = createVm,
                    onNext = { navController.navigate(Screen.CreateIngredients.route) }
                )
            }

            composable(Screen.CreateIngredients.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.CreateGraph.route)
                }
                val createVm: CreateRecipeViewModel =
                    viewModel(viewModelStoreOwner = parentEntry)
                CreateIngredientsScreen(
                    createVm = createVm,
                    onNext = { navController.navigate(Screen.CreateSteps.route) }
                )
            }

            composable(Screen.CreateSteps.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.CreateGraph.route)
                }
                // <LK>: Step 3 takes BOTH viewmodels - graph-scoped for state
                val createVm: CreateRecipeViewModel =
                    viewModel(viewModelStoreOwner = parentEntry)
                CreateStepsScreen(
                    createVm = createVm,
                    recipeVm = viewModel,
                    onSaved = {
                        // Pop the entire nested graph and return to Browse
                        navController.navigate(Screen.Browse.route) {
                            popUpTo(Screen.CreateGraph.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

// <LK>: routes - top-level destinations + nested create graph
sealed class Screen(val route: String) {
    data object Browse : Screen("browse")
    data object Favorites : Screen("favorites")
    data object Detail : Screen("detail/{recipeId}") {
        fun createRoute(recipeId: Int) = "detail/$recipeId"
    }

    // Nested create-recipe flow
    data object CreateGraph : Screen("create_graph")
    data object CreateDetails : Screen("create/details")
    data object CreateIngredients : Screen("create/ingredients")
    data object CreateSteps : Screen("create/steps")
}