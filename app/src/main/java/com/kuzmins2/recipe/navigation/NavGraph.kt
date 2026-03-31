package com.kuzmins2.recipe.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kuzmins2.recipe.ui.AddRecipeScreen
import com.kuzmins2.recipe.ui.RecipeDetailScreen
import com.kuzmins2.recipe.ui.RecipeListScreen
import com.kuzmins2.recipe.viewmodel.RecipeViewModel


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

        composable(Screen.Add.route) {
            AddRecipeScreen(
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId")
            recipeId?.let {
                RecipeDetailScreen(
                    recipeId = it,
                    viewModel = viewModel
                )
            }
        }
    }
}

// navigation/Screen.kt
sealed class Screen(val route: String) {
    data object Browse : Screen("browse")
    data object Add : Screen("add")
    data object Detail : Screen("detail/{recipeId}") {
        fun createRoute(recipeId: Int) = "detail/$recipeId"
    }
}