package com.example.gestion_automotriz.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var code by remember(initialProduct) { mutableStateOf(initialProduct?.code ?: "") }
    var name by remember(initialProduct) { mutableStateOf(initialProduct?.name ?: "") }
    var description by remember(initialProduct) { mutableStateOf(initialProduct?.description ?: "") }
    var category by remember(initialProduct) { mutableStateOf(initialProduct?.category ?: "") }
    var brand by remember(initialProduct) { mutableStateOf(initialProduct?.brand ?: "Genérica") }
    var stock by remember(initialProduct) { mutableStateOf(initialProduct?.currentStock?.toString() ?: "0") }
    var minStock by remember(initialProduct) { mutableStateOf(initialProduct?.minStock?.toString() ?: "5") }
    var targetStock by remember(initialProduct) { mutableStateOf(initialProduct?.targetStock?.toString() ?: "10") }
    var purchasePrice by remember(initialProduct) { mutableStateOf(initialProduct?.purchasePrice?.toString() ?: "0") }
    var salePrice by remember(initialProduct) { mutableStateOf(initialProduct?.salePrice?.toString() ?: "0") }
    var providerId by remember(initialProduct) { mutableStateOf(initialProduct?.providerId ?: "PROV-01") }
    var zone by remember(initialProduct) { mutableStateOf(initialProduct?.locationZone ?: "") }
    var shelf by remember(initialProduct) { mutableStateOf(initialProduct?.locationShelf ?: "") }
    var position by remember(initialProduct) { mutableStateOf(initialProduct?.locationPosition ?: "") }
    var validationError by remember(initialProduct) { mutableStateOf<String?>(null) }

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

        OutlinedTextField(value = code, onValueChange = { code = it }, enabled = !isEditing, label = { Text("Código de Producto") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Cantidad Actual") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = minStock, onValueChange = { minStock = it }, label = { Text("Existencia Mínima") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = targetStock, onValueChange = { targetStock = it }, label = { Text("Stock Objetivo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = purchasePrice, onValueChange = { purchasePrice = it }, label = { Text("Precio de Compra") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = salePrice, onValueChange = { salePrice = it }, label = { Text("Precio de Venta") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = providerId, onValueChange = { providerId = it }, label = { Text("ID de Proveedor") }, modifier = Modifier.fillMaxWidth())

        Text("Ubicación en Bodega", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        OutlinedTextField(value = zone, onValueChange = { zone = it }, label = { Text("Zona / Sector") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = shelf, onValueChange = { shelf = it }, label = { Text("Estante / Repisa") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = position, onValueChange = { position = it }, label = { Text("Posición Específica") }, modifier = Modifier.fillMaxWidth())

        validationError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val stockValue = stock.toIntOrNull()
                val minimumValue = minStock.toIntOrNull()
                val targetValue = targetStock.toIntOrNull()
                val purchaseValue = purchasePrice.toDoubleOrNull()
                val saleValue = salePrice.toDoubleOrNull()
                validationError = when {
                    code.isBlank() || name.isBlank() || category.isBlank() -> "Código, nombre y categoría son obligatorios"
                    stockValue == null || stockValue < 0 -> "El stock debe ser un número igual o mayor a cero"
                    minimumValue == null || minimumValue < 0 -> "El stock mínimo debe ser un número igual o mayor a cero"
                    targetValue == null || targetValue < minimumValue -> "El stock objetivo debe ser mayor o igual al mínimo"
                    purchaseValue == null || purchaseValue < 0 || saleValue == null || saleValue < 0 ->
                        "Los precios deben ser números iguales o mayores a cero"
                    providerId.isBlank() || zone.isBlank() || shelf.isBlank() ->
                        "Proveedor, zona y estante son obligatorios"
                    else -> null
                }
                if (
                    validationError == null &&
                    stockValue != null &&
                    minimumValue != null &&
                    targetValue != null &&
                    purchaseValue != null &&
                    saleValue != null
                ) {
                    onSaveProduct(
                        ProductEntity(
                            code = code.trim(),
                            name = name.trim(),
                            description = description.trim(),
                            category = category.trim(),
                            brand = brand.trim(),
                            currentStock = stockValue,
                            minStock = minimumValue,
                            targetStock = targetValue,
                            purchasePrice = purchaseValue,
                            salePrice = saleValue,
                            providerId = providerId.trim(),
                            locationZone = zone.trim(),
                            locationShelf = shelf.trim(),
                            locationPosition = position.trim()
                        )
                    )
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditing) "Actualizar Producto" else "Guardar Producto")
        }
    }
}
