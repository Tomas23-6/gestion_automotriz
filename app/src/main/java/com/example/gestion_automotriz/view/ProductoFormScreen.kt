package com.example.gestion_automotriz.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_automotriz.model.ProductEntity

@Composable
fun ProductoFormScreen(
    user: String,
    initialProduct: ProductEntity? = null,
    onSaveProduct: (ProductEntity) -> Unit,
    onBack: () -> Unit
) {
    var code by remember { mutableStateOf(initialProduct?.code ?: "") }
    var name by remember { mutableStateOf(initialProduct?.name ?: "") }
    var category by remember { mutableStateOf(initialProduct?.category ?: "") }
    var brand by remember { mutableStateOf(initialProduct?.brand ?: "") }
    var stock by remember { mutableStateOf(initialProduct?.currentStock?.toString() ?: "") }
    var minStock by remember { mutableStateOf(initialProduct?.minStock?.toString() ?: "5") }
    var providerId by remember { mutableStateOf(initialProduct?.providerId ?: "PROV-01") }
    var zone by remember { mutableStateOf(initialProduct?.locationZone ?: "") }
    var shelf by remember { mutableStateOf(initialProduct?.locationShelf ?: "") }
    var position by remember { mutableStateOf(initialProduct?.locationPosition ?: "") }

    val isEditing = initialProduct != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            if (isEditing) "Modificar Producto / Reubicar" else "Registrar Nuevo Producto",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = code, onValueChange = { if (!isEditing) code = it }, enabled = !isEditing, label = { Text("Código de Producto") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre / Descripción") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría (ej: Lubricantes)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Cantidad Actual") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = minStock, onValueChange = { minStock = it }, label = { Text("Existencia Mínima") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = providerId, onValueChange = { providerId = it }, label = { Text("ID de Proveedor") }, modifier = Modifier.fillMaxWidth())

        Text("Ubicación en Bodega", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        OutlinedTextField(value = zone, onValueChange = { zone = it }, label = { Text("Zona / Sector") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = shelf, onValueChange = { shelf = it }, label = { Text("Estante / Repisa") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = position, onValueChange = { position = it }, label = { Text("Posición Específica") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val minVal = minStock.toIntOrNull() ?: 5
                val product = ProductEntity(
                    code = code,
                    name = name,
                    category = category,
                    brand = brand,
                    currentStock = stock.toIntOrNull() ?: 0,
                    minStock = minVal,
                    targetStock = minVal * 2,
                    providerId = providerId,
                    locationZone = zone,
                    locationShelf = shelf,
                    locationPosition = position
                )
                onSaveProduct(product)
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditing) "Actualizar Producto" else "Guardar Producto")
        }
    }
}