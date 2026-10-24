package com.example.gestion_automotriz.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MovimientoFormScreen(
    onRegisterMovement: suspend (code: String, qty: Int, type: String, reason: String) -> Result<Unit>
) {
    var code by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("VENTA") }
    var reason by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val movementTypes = listOf("INGRESO", "VENTA", "CONSUMO", "AJUSTE", "DEVOLUCION")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Registro de Movimiento de Inventario", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Código de Producto") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Cantidad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = reason, onValueChange = { reason = it }, label = { Text("Motivo / Orden de Trabajo") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))
        Text("Tipo de Operación:", style = MaterialTheme.typography.bodyMedium)

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            movementTypes.chunked(3).forEach { rowTypes ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowTypes.forEach { itemType ->
                        FilterChip(
                            selected = type == itemType,
                            onClick = { type = itemType },
                            label = { Text(itemType) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val qtyInt = quantity.toIntOrNull() ?: 0
                if (code.isBlank() || qtyInt <= 0) {
                    message = "Error: Ingrese código válido y cantidad mayor a 0"
                    isError = true
                    return@Button
                }
                scope.launch {
                    val result = onRegisterMovement(code, qtyInt, type, reason)
                    result.onSuccess {
                        message = "Movimiento [$type] registrado con éxito"
                        isError = false
                        code = ""; quantity = ""; reason = ""
                    }.onFailure { err ->
                        message = err.message ?: "Error al procesar"
                        isError = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar Movimiento")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
        }
    }
}