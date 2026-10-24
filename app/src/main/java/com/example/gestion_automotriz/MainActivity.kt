package com.example.gestion_automotriz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gestion_automotriz.model.AppDatabase
import com.example.gestion_automotriz.navigation.AppNav
import com.example.gestion_automotriz.repository.InventoryRepository
import com.example.gestion_automotriz.ui.theme.AppTheme
import com.example.gestion_automotriz.ui.theme.login.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la app ocupe el diseño completo de borde a borde en el dispositivo
        enableEdgeToEdge()

        // 1. Inicialización de la base de datos SQLite con Room
        val database = AppDatabase.getInstance(this)

        // 2. Creación del repositorio inyectando la base de datos
        val inventoryRepository = InventoryRepository(database)

        // 3. Instancia del ViewModel para la gestión del inicio de sesión
        val loginViewModel = LoginViewModel()

        // 4. Montaje de la interfaz gráfica con Compose
        setContent {
            AppTheme {
                AppNav(
                    loginViewModel = loginViewModel,
                    inventoryRepository = inventoryRepository
                )
            }
        }
    }
}