package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.ApiClient
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.TokenDataStore
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.DiegoHerrera22AppMoviles007D_EV2_DHerrera_JArayaTheme
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.AuthViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.RegionViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.CartScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.CheckoutDetailsScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.CheckoutFailureScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.CheckoutSuccessScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.HomeScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.LoginScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.ProductDetailScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.RegisterScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.UserProfileScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.backoffice.AddProductScreen
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.backoffice.BackOfficeListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApiClient.init(TokenDataStore(applicationContext))

        WindowCompat.setDecorFitsSystemWindows(window, true)

        window.decorView.post {
            WindowCompat.getInsetsController(window, window.decorView)?.let { controller ->
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                controller.show(WindowInsetsCompat.Type.systemBars())
            }
        }
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
    val context = LocalContext.current

    // ViewModels que deben ser únicos en toda la app
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.provideFactory(context)
    )
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

            composable(
                route = "profile/{email}",
                arguments = listOf(navArgument("email") { type = NavType.StringType })
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("shop") }
                val email = backStackEntry.arguments?.getString("email")
                UserProfileScreen(email = email, navController = navController, parentEntry = parentEntry)
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

            composable("checkout/details") { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("shop") }
                CheckoutDetailsScreen(navController = navController, parentEntry = parentEntry)
            }

            composable("checkout/success") { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("shop") }
                CheckoutSuccessScreen(navController = navController, parentEntry = parentEntry)
            }

            composable("checkout/failure") { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("shop") }
                CheckoutFailureScreen(navController = navController, parentEntry = parentEntry)
            }
        }
    }
}