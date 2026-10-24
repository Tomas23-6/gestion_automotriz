package com.example.gestion_automotriz.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestion_automotriz.model.ProductEntity
import com.example.gestion_automotriz.repository.InventoryRepository
import com.example.gestion_automotriz.ui.theme.home.MuestraDatosScreen
import com.example.gestion_automotriz.ui.theme.login.LoginViewModel
import com.example.gestion_automotriz.view.*
import kotlinx.coroutines.launch

@Composable
fun AppNav(
    loginViewModel: LoginViewModel,
    inventoryRepository: InventoryRepository
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController = navController, vm = loginViewModel)
        }

        composable(
            route = "muestraDatos/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"
            val products by inventoryRepository.getAllProducts().collectAsState(initial = emptyList())

            DrawerMenu(user = user, navController = navController) { openDrawer ->
                MuestraDatosScreen(
                    user = user,
                    products = products,
                    onOpenDrawer = openDrawer,
                    onEditProduct = { product ->
                        editingProduct = product
                        navController.navigate("productoForm/$user")
                    }
                )
            }
        }

        composable(
            route = "productoForm/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"

            DrawerMenu(user = user, navController = navController) {
                ProductoFormScreen(
                    user = user,
                    initialProduct = editingProduct,
                    onSaveProduct = { product ->
                        scope.launch { inventoryRepository.saveProduct(product) }
                        editingProduct = null
                    },
                    onBack = {
                        editingProduct = null
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = "movimientoForm/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"

            DrawerMenu(user = user, navController = navController) {
                MovimientoFormScreen(
                    onRegisterMovement = { code, qty, type, reason ->
                        inventoryRepository.registerMovement(code, qty, type, reason, user)
                    }
                )
            }
        }

        composable(
            route = "historial/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"
            val movements by inventoryRepository.getAllMovements().collectAsState(initial = emptyList())

            DrawerMenu(user = user, navController = navController) {
                HistorialMovimientosScreen(movements = movements)
            }
        }

        composable(
            route = "reposicion/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"
            val lowStockProducts by inventoryRepository.getLowStockProducts().collectAsState(initial = emptyList())
            val providers by inventoryRepository.getAllProviders().collectAsState(initial = emptyList())

            DrawerMenu(user = user, navController = navController) {
                ReposicionScreen(lowStockProducts = lowStockProducts, providers = providers)
            }
        }

        composable(
            route = "proveedores/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"
            val providers by inventoryRepository.getAllProviders().collectAsState(initial = emptyList())

            DrawerMenu(user = user, navController = navController) {
                ProveedorScreen(
                    providers = providers,
                    onSaveProvider = { prov -> scope.launch { inventoryRepository.saveProvider(prov) } }
                )
            }
        }
    }
}