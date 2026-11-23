package com.example.gestion_automotriz.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_automotriz.model.ProviderEntity

@Composable
fun ProveedorScreen(
    providers: List<ProviderEntity>,
    onSaveProvider: (ProviderEntity) -> Unit,
    onDeleteProvider: (ProviderEntity) -> Unit
) {
    var editingProvider by remember { mutableStateOf<ProviderEntity?>(null) }
    var deletingProvider by remember { mutableStateOf<ProviderEntity?>(null) }
    var id by remember(editingProvider) { mutableStateOf(editingProvider?.providerId ?: "") }
    var name by remember(editingProvider) { mutableStateOf(editingProvider?.name ?: "") }
    var contactName by remember(editingProvider) { mutableStateOf(editingProvider?.contactName ?: "") }
    var email by remember(editingProvider) { mutableStateOf(editingProvider?.email ?: "") }
    var phone by remember(editingProvider) { mutableStateOf(editingProvider?.phone ?: "") }
    var validationError by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestión de Proveedores", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            enabled = editingProvider == null,
            label = { Text("ID Proveedor (ej: PROV-01)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre Proveedor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = contactName, onValueChange = { contactName = it }, label = { Text("Nombre de Contacto") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

        validationError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                validationError = when {
                    id.isBlank() || name.isBlank() -> "El ID y el nombre del proveedor son obligatorios"
                    else -> null
                }
                if (validationError == null) {
                    onSaveProvider(
                        ProviderEntity(
                            providerId = id.trim(),
                            name = name.trim(),
                            contactName = contactName.trim(),
                            email = email.trim(),
                            phone = phone.trim(),
                            category = editingProvider?.category ?: "General"
                        )
                    )
                    editingProvider = null
                    id = ""
                    name = ""
                    contactName = ""
                    email = ""
                    phone = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editingProvider == null) "Guardar Proveedor" else "Actualizar Proveedor")
        }
        if (editingProvider != null) {
            TextButton(
                onClick = {
                    editingProvider = null
                    validationError = null
                    id = ""
                    name = ""
                    contactName = ""
                    email = ""
                    phone = ""
                }
            ) {
                Text("Cancelar edición")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Lista de Proveedores", style = MaterialTheme.typography.titleMedium)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(providers, key = { it.providerId }) { provider ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "${provider.name} (${provider.providerId})",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Contacto: ${provider.contactName}")
                        Text("Email: ${provider.email} | Teléfono: ${provider.phone}")
                        Row {
                            TextButton(
                                onClick = {
                                    editingProvider = provider
                                    validationError = null
                                }
                            ) {
                                Text("Editar")
                            }
                            TextButton(onClick = { deletingProvider = provider }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    deletingProvider?.let { provider ->
        AlertDialog(
            onDismissRequest = { deletingProvider = null },
            title = { Text("Eliminar proveedor") },
            text = { Text("¿Deseas eliminar ${provider.name} (${provider.providerId})?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteProvider(provider)
                        deletingProvider = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingProvider = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
