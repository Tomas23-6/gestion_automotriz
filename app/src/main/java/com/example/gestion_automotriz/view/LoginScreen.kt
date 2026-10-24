package com.example.gestion_automotriz.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gestion_automotriz.ui.theme.login.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    vm: LoginViewModel
) {
    val state = vm.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Gestión Taller Automotriz", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = { vm.onUsernameChange(it) },
            label = { Text("Usuario (ej: admin)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { vm.onPasswordChange(it) },
            label = { Text("PIN de acceso (ej: 1234)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        state.error?.let { err ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = err, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                vm.submit { username ->
                    navController.navigate("muestraDatos/$username") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Ingresar al Sistema")
            }
        }
    }
}