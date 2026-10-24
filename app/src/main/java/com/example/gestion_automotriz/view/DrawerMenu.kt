package com.example.gestion_automotriz.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(
    user: String,
    navController: NavController,
    content: @Composable (openDrawer: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun navigateTo(route: String) {
        scope.launch { drawerState.close() }
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Usuario: $user",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                HorizontalDivider()

                NavigationDrawerItem(label = { Text("Consulta de Inventario") }, selected = false, onClick = { navigateTo("muestraDatos/$user") })
                NavigationDrawerItem(label = { Text("Registrar Producto") }, selected = false, onClick = { navigateTo("productoForm/$user") })
                NavigationDrawerItem(label = { Text("Registrar Movimiento") }, selected = false, onClick = { navigateTo("movimientoForm/$user") })
                NavigationDrawerItem(label = { Text("Historial de Movimientos") }, selected = false, onClick = { navigateTo("historial/$user") })
                NavigationDrawerItem(label = { Text("Módulo de Reposición") }, selected = false, onClick = { navigateTo("reposicion/$user") })
                NavigationDrawerItem(label = { Text("Gestión de Proveedores") }, selected = false, onClick = { navigateTo("proveedores/$user") })

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    },
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }
    ) {
        content { scope.launch { drawerState.open() } }
    }
}