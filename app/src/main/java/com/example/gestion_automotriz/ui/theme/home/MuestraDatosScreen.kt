package com.example.gestion_automotriz.ui.theme.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestion_automotriz.model.ProductEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuestraDatosScreen(
    user: String,
    products: List<ProductEntity>,
    onOpenDrawer: () -> Unit,
    onEditProduct: (ProductEntity) -> Unit
) {
    var search by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todas") }

    val categories = remember(products) {
        listOf("Todas") + products.map { it.category }.distinct()
    }

    val filteredProducts = products.filter { p ->
        val matchesSearch = p.name.contains(search, ignoreCase = true) || p.code.contains(search, ignoreCase = true)
        val matchesCategory = selectedCategory == "Todas" || p.category.equals(selectedCategory, ignoreCase = true)
        matchesSearch && matchesCategory
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario - $user") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Text("☰", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Buscar por nombre o código") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Selector de Categoría
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.take(4).forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredProducts, key = { it.code }) { product ->
                    val isLowStock = product.currentStock <= product.minStock
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isLowStock) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                                TextButton(onClick = { onEditProduct(product) }) {
                                    Text("Editar / Mover")
                                }
                            }
                            Text(text = "Código: ${product.code} | Categoría: ${product.category} | Marca: ${product.brand}")
                            Text(text = "Ubicación: Zona ${product.locationZone} - ${product.locationShelf} (${product.locationPosition})")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Stock Disponible: ${product.currentStock} (Mínimo: ${product.minStock})",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}