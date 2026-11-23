package com.example.gestion_automotriz.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestion_automotriz.model.ProductEntity
import com.example.gestion_automotriz.ui.theme.home.MuestraDatosScreen
import com.example.gestion_automotriz.ui.theme.login.LoginViewModel
import com.example.gestion_automotriz.viewmodel.InventoryViewModel
import com.example.gestion_automotriz.viewmodel.PostViewModel
import com.example.gestion_automotriz.view.*

@Composable
fun AppNav(
    loginViewModel: LoginViewModel,
    inventoryViewModel: InventoryViewModel,
    postViewModel: PostViewModel
) {
    val navController = rememberNavController()
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
            val products by inventoryViewModel.products.collectAsState()

            DrawerMenu(user = user, navController = navController) { openDrawer ->
                MuestraDatosScreen(
                    user = user,
                    products = products,
                    onOpenDrawer = openDrawer,
                    onEditProduct = { product ->
                        editingProduct = product
                        navController.navigate("productoForm/$user")
                    },
                    onDeleteProduct = { product ->
                        inventoryViewModel.deleteProduct(product.code)
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
                        inventoryViewModel.saveProduct(product)
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
                        inventoryViewModel.registerMovement(code, qty, type, reason, user)
                    }
                )
            }
        }

        composable(
            route = "historial/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"
            val movements by inventoryViewModel.movements.collectAsState()

            DrawerMenu(user = user, navController = navController) {
                HistorialMovimientosScreen(movements = movements)
            }
        }

        composable(
            route = "reposicion/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"
            val lowStockProducts by inventoryViewModel.lowStockProducts.collectAsState()
            val providers by inventoryViewModel.providers.collectAsState()

            DrawerMenu(user = user, navController = navController) {
                ReposicionScreen(lowStockProducts = lowStockProducts, providers = providers)
            }
        }

        composable(
            route = "proveedores/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"
            val providers by inventoryViewModel.providers.collectAsState()

            DrawerMenu(user = user, navController = navController) {
                ProveedorScreen(
                    providers = providers,
                    onSaveProvider = { prov -> inventoryViewModel.saveProvider(prov) },
                    onDeleteProvider = { provider ->
                        inventoryViewModel.deleteProvider(provider.providerId)
                    }
                )
            }
        }

        composable(
            route = "publicaciones/{user}",
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Propietario"
            val state by postViewModel.uiState

            DrawerMenu(user = user, navController = navController) { openDrawer ->
                PostScreen(
                    state = state,
                    onOpenDrawer = openDrawer,
                    onReload = postViewModel::loadPosts,
                    onCreate = postViewModel::createPost,
                    onUpdate = postViewModel::updatePost,
                    onDelete = postViewModel::deletePost
                )
            }
        }
    }
}