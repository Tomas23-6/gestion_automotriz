package com.example.gestion_automotriz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestion_automotriz.model.MovementEntity
import com.example.gestion_automotriz.model.ProductEntity
import com.example.gestion_automotriz.model.ProviderEntity
import com.example.gestion_automotriz.repository.InventoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val repository: InventoryRepository
) : ViewModel() {

    val products: StateFlow<List<ProductEntity>> = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStockProducts: StateFlow<List<ProductEntity>> = repository.getLowStockProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val movements: StateFlow<List<MovementEntity>> = repository.getAllMovements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val providers: StateFlow<List<ProviderEntity>> = repository.getAllProviders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        sync()
    }

    fun sync() {
        viewModelScope.launch { repository.syncFromBackend() }
    }

    fun saveProduct(product: ProductEntity) {
        viewModelScope.launch { repository.saveProduct(product) }
    }

    fun deleteProduct(code: String) {
        viewModelScope.launch { repository.deleteProduct(code) }
    }

    fun saveProvider(provider: ProviderEntity) {
        viewModelScope.launch { repository.saveProvider(provider) }
    }

    fun deleteProvider(id: String) {
        viewModelScope.launch { repository.deleteProvider(id) }
    }

    suspend fun registerMovement(
        code: String,
        qty: Int,
        type: String,
        reason: String,
        userId: String
    ): Result<Unit> = repository.registerMovement(code, qty, type, reason, userId)

    class Factory(private val repository: InventoryRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
                throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
            }
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(repository) as T
        }
    }
}