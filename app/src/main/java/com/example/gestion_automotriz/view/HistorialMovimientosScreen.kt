package com.example.gestion_automotriz.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_automotriz.model.MovementEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistorialMovimientosScreen(movements: List<MovementEntity>) {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Historial de Movimientos y Auditoría", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (movements.isEmpty()) {
            Text("No hay movimientos registrados.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(movements, key = { it.id }) { mov ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Tipo: ${mov.movementType}", style = MaterialTheme.typography.titleMedium)
                                Text(text = formatter.format(Date(mov.timestamp)), style = MaterialTheme.typography.bodySmall)
                            }
                            Text(text = "Producto: ${mov.productCode} | Cantidad: ${mov.quantity}")
                            Text(text = "Stock: ${mov.previousStock} ➔ ${mov.resultingStock}")
                            Text(text = "Motivo: ${mov.reason.ifBlank { "Sin detalle" }}")
                            Text(text = "Registrado por: ${mov.registeredByUserId}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}