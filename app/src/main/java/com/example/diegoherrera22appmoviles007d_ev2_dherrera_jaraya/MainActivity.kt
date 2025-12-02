package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.DiegoHerrera22AppMoviles007D_EV2_DHerrera_JArayaTheme
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.AuthViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.HomeScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.LoginScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.RegisterScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.navigation
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.ProductDetailScreen
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.RegionViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.CartScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.CheckoutResultTabsScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.backoffice.AddProductScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.backoffice.BackOfficeListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiegoHerrera22AppMoviles007D_EV2_DHerrera_JArayaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // ViewModels que deben ser únicos en toda la app
    val authViewModel: AuthViewModel = viewModel()
    val regionViewModel: RegionViewModel = viewModel() // ← **IMPORTANTE**

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                viewModel = authViewModel,
                regionViewModel = regionViewModel // ← **PASADO AQUÍ**
            )
        }

        // Graph padre para HOME y Detalles
        navigation(startDestination = "home/{email}", route = "shop") {

            composable(
                route = "home/{email}",
                arguments = listOf(navArgument("email") { type = NavType.StringType })
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("shop")
                }
                val email = backStackEntry.arguments?.getString("email")
                HomeScreen(email = email, navController = navController, parentEntry = parentEntry)
            }

            composable(
                route = "product/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("shop")
                }
                val id = backStackEntry.arguments?.getString("id") ?: return@composable
                ProductDetailScreen(productId = id, navController = navController, parentEntry = parentEntry)
            }

            composable("backoffice") {
                BackOfficeListScreen(
                    onAddProduct = { navController.navigate("backoffice/add") }
                )
            }

            composable("backoffice/add") {
                AddProductScreen(navController = navController)
            }

            composable("cart") { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("shop") }
                CartScreen(navController = navController, parentEntry = parentEntry)
            }

            composable("checkout/results") { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("shop") }
                CheckoutResultTabsScreen(navController = navController, parentEntry = parentEntry)
            }
        }
    }
}