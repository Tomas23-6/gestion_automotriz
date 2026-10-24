package com.example.gestion_automotriz.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole { PROPIETARIO, OPERADOR }

data class UserSession(
    val userId: String,
    val username: String,
    val role: UserRole
)

data class LoginCredentials(
    val username: String = "",
    val pin: String = ""
)

// Tabla de Productos e Insumos Automotrices
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val code: String,
    val name: String,
    val description: String = "",
    val category: String,
    val brand: String = "Genérica",
    val currentStock: Int,
    val minStock: Int,
    val targetStock: Int,
    val purchasePrice: Double = 0.0,
    val salePrice: Double = 0.0,
    val status: String = "Disponible",
    val providerId: String = "PROV-01",
    val locationZone: String,
    val locationShelf: String,
    val locationPosition: String = "N/A",
    val lastUpdate: Long = System.currentTimeMillis()
)

// Tabla de Historial y Trazabilidad
@Entity(tableName = "movements")
data class MovementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productCode: String,
    val movementType: String, // INGRESO, VENTA, CONSUMO, AJUSTE, DEVOLUCION
    val quantity: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val reason: String,
    val previousStock: Int,
    val resultingStock: Int,
    val registeredByUserId: String
)

// Tabla de Proveedores Ficticios
@Entity(tableName = "providers")
data class ProviderEntity(
    @PrimaryKey val providerId: String,
    val name: String,
    val contactName: String,
    val email: String,
    val phone: String,
    val category: String = "General"
)