package com.example.gestion_automotriz.model

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE currentStock <= minStock ORDER BY name ASC")
    fun getLowStockProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE code = :code LIMIT 1")
    suspend fun getProductByCode(code: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProduct(product: ProductEntity)

    @Query("UPDATE products SET currentStock = :newStock, lastUpdate = :timestamp WHERE code = :code")
    suspend fun updateStock(code: String, newStock: Int, timestamp: Long)
}

@Dao
interface MovementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovement(movement: MovementEntity)

    @Query("SELECT * FROM movements ORDER BY timestamp DESC")
    fun getAllMovements(): Flow<List<MovementEntity>>

    @Query("SELECT * FROM movements WHERE productCode = :code ORDER BY timestamp DESC")
    fun getMovementsByProduct(code: String): Flow<List<MovementEntity>>
}

@Dao
interface ProviderDao {
    @Query("SELECT * FROM providers ORDER BY name ASC")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    @Query("SELECT * FROM providers WHERE providerId = :id LIMIT 1")
    suspend fun getProviderById(id: String): ProviderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: ProviderEntity)
}

@Database(
    entities = [ProductEntity::class, MovementEntity::class, ProviderEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun movementDao(): MovementDao
    abstract fun providerDao(): ProviderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestion_automotriz.db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}