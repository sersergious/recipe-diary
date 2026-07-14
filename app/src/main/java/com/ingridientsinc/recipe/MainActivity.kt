package com.ingridientsinc.recipe


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ingridientsinc.recipe.navigation.NavGraph
import com.ingridientsinc.recipe.navigation.Screen
import com.kuzmins2.recipe.ui.theme.RecipesTheme
import com.ingridientsinc.recipe.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
fun RecipesApp(viewModel: RecipeViewModel? = null) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    // Use the provided viewModel if available, otherwise get it from Hilt (but not in Preview)
    val actualViewModel = viewModel ?: if (LocalInspectionMode.current) null else hiltViewModel()

    //<LK>: Prevents stacking duplicate destinations and preserves state across tab switches
    fun navigatetoTab(route: String){
        navController.navigate(route){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
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
                selected = currentRoute?.startsWith("create/") == true,
                onClick = { navigatetoTab(Screen.CreateGraph.route) }
            )
            item(
                icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                label = { Text("Favorites") },
                selected = currentRoute == Screen.Favorites.route,
                onClick = { navigatetoTab(Screen.Favorites.route) }
            )
        }
    ) {
        if (actualViewModel != null) {
            NavGraph(
                navController = navController,
                viewModel = actualViewModel
            )
        } else {
            // Preview placeholder to avoid crash when ViewModel cannot be instantiated
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Recipe Diary App",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
