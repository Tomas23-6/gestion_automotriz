package com.example.gestion_automotriz.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gestion_automotriz.model.ProductEntity
import com.example.gestion_automotriz.model.ProviderEntity

@Composable
fun ReposicionScreen(
    lowStockProducts: List<ProductEntity>,
    providers: List<ProviderEntity>
) {
    val context = LocalContext.current
    var selectedProviderId by remember { mutableStateOf("TODOS") }

    val providerMap = remember(providers) { providers.associateBy { it.providerId } }
    val groupedProducts = remember(lowStockProducts) { lowStockProducts.groupBy { it.providerId } }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Módulo de Reposición de Insumos", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            groupedProducts.forEach { (provId, products) ->
                val provider = providerMap[provId]
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Proveedor: ${provider?.name ?: provId}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(text = "Correo: ${provider?.email ?: "Sin correo registrado"}")
                            Spacer(modifier = Modifier.height(8.dp))

                            products.forEach { p ->
                                val suggested = (p.targetStock - p.currentStock).coerceAtLeast(1)
                                Text(text = "• ${p.name} (Cód: ${p.code}) | Pedir: $suggested unid.")
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    openSupplierEmailDraft(
                                        context = context,
                                        products = products,
                                        email = provider?.email ?: "proveedor@ficticio.cl",
                                        providerName = provider?.name ?: provId
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Generar Borrador para ${provider?.name ?: provId}")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun openSupplierEmailDraft(
    context: Context,
    products: List<ProductEntity>,
    email: String,
    providerName: String
) {
    val body = StringBuilder().apply {
        append("Estimados de $providerName,\n\nJunto con saludar, solicito cotización para los siguientes insumos:\n\n")
        products.forEach { p ->
            val qty = (p.targetStock - p.currentStock).coerceAtLeast(1)
            append("• ${p.name} (Código: ${p.code}) - Cantidad requerida: $qty\n")
        }
        append("\nAtentamente,\nTaller Mecánico Automotriz\n")
    }.toString()

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, "Solicitud de Reposición de Productos")
        putExtra(Intent.EXTRA_TEXT, body)
    }
    context.startActivity(intent)
}