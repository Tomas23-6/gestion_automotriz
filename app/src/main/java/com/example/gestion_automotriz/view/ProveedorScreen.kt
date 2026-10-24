package com.example.gestion_automotriz.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_automotriz.model.ProviderEntity

@Composable
fun ProveedorScreen(
    providers: List<ProviderEntity>,
    onSaveProvider: (ProviderEntity) -> Unit
) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestión de Proveedores Ficticios", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("ID Proveedor (ej: PROV-01)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre Proveedor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (id.isNotBlank() && name.isNotBlank()) {
                    onSaveProvider(ProviderEntity(id, name, name, email, phone))
                    id = ""; name = ""; email = ""; phone = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Proveedor")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Lista de Proveedores", style = MaterialTheme.typography.titleMedium)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(providers, key = { it.providerId }) { prov ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "${prov.name} (${prov.providerId})", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Email: ${prov.email} | Teléfono: ${prov.phone}")
                    }
                }
            }
        }
    }
}