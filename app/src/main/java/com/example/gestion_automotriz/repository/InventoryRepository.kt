package com.example.gestion_automotriz.repository

import com.example.gestion_automotriz.model.*
import kotlinx.coroutines.flow.Flow

class AuthRepository {
    fun login(credentials: LoginCredentials): Result<UserSession> {
        return when {
            credentials.username.equals("admin", ignoreCase = true) && credentials.pin == "1234" -> {
                Result.success(UserSession("1", "Propietario Taller", UserRole.PROPIETARIO))
            }
            credentials.username.equals("operador", ignoreCase = true) && credentials.pin == "0000" -> {
                Result.success(UserSession("2", "Asistente Mecánico", UserRole.OPERADOR))
            }
            else -> Result.failure(Exception("Credenciales incorrectas (Pruebe admin/1234 o operador/0000)"))
        }
    }
}

class InventoryRepository(private val db: AppDatabase) {

    fun getAllProducts(): Flow<List<ProductEntity>> = db.productDao().getAllProducts()
    fun getLowStockProducts(): Flow<List<ProductEntity>> = db.productDao().getLowStockProducts()
    fun getAllMovements(): Flow<List<MovementEntity>> = db.movementDao().getAllMovements()
    fun getAllProviders(): Flow<List<ProviderEntity>> = db.providerDao().getAllProviders()

    suspend fun saveProduct(product: ProductEntity) {
        db.productDao().insertOrUpdateProduct(product)
    }

    suspend fun saveProvider(provider: ProviderEntity) {
        db.providerDao().insertProvider(provider)
    }

    suspend fun getProviderById(id: String): ProviderEntity? {
        return db.providerDao().getProviderById(id)
    }

    // Registro unificado de movimientos con validación antinegativos
    suspend fun registerMovement(
        code: String,
        qty: Int,
        type: String, // INGRESO, VENTA, CONSUMO, AJUSTE, DEVOLUCION
        reason: String,
        userId: String
    ): Result<Unit> {
        if (qty <= 0) return Result.failure(Exception("La cantidad debe ser mayor a cero"))

        val product = db.productDao().getProductByCode(code)
            ?: return Result.failure(Exception("El producto con código $code no existe"))

        val isOutflow = type in listOf("VENTA", "CONSUMO", "DEVOLUCION")

        if (isOutflow && product.currentStock < qty) {
            return Result.failure(Exception("Stock insuficiente. Disponible: ${product.currentStock}, Solicitado: $qty"))
        }

        val previousStock = product.currentStock
        val resultingStock = if (isOutflow) previousStock - qty else previousStock + qty

        // Actualización de stock e historial dentro del repositorio
        db.productDao().updateStock(code, resultingStock, System.currentTimeMillis())

        val movement = MovementEntity(
            productCode = code,
            movementType = type,
            quantity = qty,
            reason = reason,
            previousStock = previousStock,
            resultingStock = resultingStock,
            registeredByUserId = userId
        )
        db.movementDao().insertMovement(movement)

        return Result.success(Unit)
    }
}