package com.example.gestion_automotriz.data.remote

import com.example.gestion_automotriz.model.MovementEntity
import com.example.gestion_automotriz.model.ProductEntity
import com.example.gestion_automotriz.model.ProviderEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BackendApiService {
    // Productos
    @GET("api/productos")
    suspend fun getProductos(): List<ProductEntity>

    @POST("api/productos")
    suspend fun saveProducto(@Body product: ProductEntity): ProductEntity

    @PUT("api/productos/{code}")
    suspend fun updateProducto(@Path("code") code: String, @Body product: ProductEntity): ProductEntity

    @DELETE("api/productos/{code}")
    suspend fun deleteProducto(@Path("code") code: String)

    // Movimientos
    @GET("api/movimientos")
    suspend fun getMovimientos(): List<MovementEntity>

    @POST("api/movimientos")
    suspend fun createMovimiento(@Body movement: MovementEntity): MovementEntity

    // Proveedores
    @GET("api/proveedores")
    suspend fun getProveedores(): List<ProviderEntity>

    @POST("api/proveedores")
    suspend fun saveProveedor(@Body provider: ProviderEntity): ProviderEntity

    @DELETE("api/proveedores/{id}")
    suspend fun deleteProveedor(@Path("id") id: String)
}

object BackendRetrofitInstance {
    // 10.0.2.2 apunta al localhost de tu PC desde el emulador Android
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: BackendApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApiService::class.java)
    }
}