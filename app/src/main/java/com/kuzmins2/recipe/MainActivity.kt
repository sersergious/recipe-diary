package com.kuzmins2.recipe


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuzmins2.recipe.navigation.NavGraph
import com.kuzmins2.recipe.navigation.Screen
import com.kuzmins2.recipe.ui.theme.RecipesTheme
import com.kuzmins2.recipe.viewmodel.RecipeViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipesTheme {
                RecipesApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun RecipesApp(viewModel: RecipeViewModel = viewModel()) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                icon = { Icon(Icons.Default.Home, contentDescription = "Browse") },
                label = { Text("Browse") },
                selected = currentRoute == Screen.Browse.route,
                onClick = { navController.navigate(Screen.Browse.route) }
            )
            item(
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                label = { Text("Add") },
                selected = currentRoute == Screen.Add.route,
                onClick = { navController.navigate(Screen.Add.route) }
            )
        }
    ) {
        NavGraph(
            navController = navController,
            viewModel = viewModel
        )
    }
}