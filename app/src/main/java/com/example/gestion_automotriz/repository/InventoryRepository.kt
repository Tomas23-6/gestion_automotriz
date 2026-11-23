package com.example.gestion_automotriz.repository

import com.example.gestion_automotriz.data.remote.BackendApiService
import com.example.gestion_automotriz.data.remote.BackendRetrofitInstance
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

class InventoryRepository(
    private val db: AppDatabase,
    private val backendApi: BackendApiService = BackendRetrofitInstance.api
) {
    fun getAllProducts(): Flow<List<ProductEntity>> = db.productDao().getAllProducts()
    fun getLowStockProducts(): Flow<List<ProductEntity>> = db.productDao().getLowStockProducts()
    fun getAllMovements(): Flow<List<MovementEntity>> = db.movementDao().getAllMovements()
    fun getAllProviders(): Flow<List<ProviderEntity>> = db.providerDao().getAllProviders()

    suspend fun syncFromBackend() {
        runCatching {
            backendApi.getProveedores().forEach { db.providerDao().insertProvider(it) }
            backendApi.getProductos().forEach { db.productDao().insertOrUpdateProduct(it) }
            backendApi.getMovimientos().forEach { db.movementDao().insertMovement(it) }
        }
    }

    suspend fun saveProduct(product: ProductEntity) {
        val saved = runCatching { backendApi.saveProducto(product) }.getOrDefault(product)
        db.productDao().insertOrUpdateProduct(saved)
    }

    suspend fun deleteProduct(code: String) {
        runCatching { backendApi.deleteProducto(code) }
        db.productDao().deleteProduct(code)
    }

    suspend fun saveProvider(provider: ProviderEntity) {
        val saved = runCatching { backendApi.saveProveedor(provider) }.getOrDefault(provider)
        db.providerDao().insertProvider(saved)
    }

    suspend fun deleteProvider(id: String) {
        runCatching { backendApi.deleteProveedor(id) }
        db.providerDao().deleteProvider(id)
    }

    suspend fun getProviderById(id: String): ProviderEntity? {
        return db.providerDao().getProviderById(id)
    }

    suspend fun registerMovement(
        code: String,
        qty: Int,
        type: String,
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
        val now = System.currentTimeMillis()

        val movement = MovementEntity(
            productCode = code,
            movementType = type,
            quantity = qty,
            timestamp = now,
            reason = reason,
            previousStock = previousStock,
            resultingStock = resultingStock,
            registeredByUserId = userId
        )

        val remoteResult = runCatching { backendApi.createMovimiento(movement) }
        if (remoteResult.isFailure) {
            return Result.failure(Exception("Error al registrar en microservicio: ${remoteResult.exceptionOrNull()?.message}"))
        }

        db.productDao().updateStock(code, resultingStock, now)
        db.movementDao().insertMovement(remoteResult.getOrDefault(movement))

        return Result.success(Unit)
    }
}