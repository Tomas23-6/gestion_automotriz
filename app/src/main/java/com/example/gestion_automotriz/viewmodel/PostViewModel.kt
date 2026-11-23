package com.example.gestion_automotriz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestion_automotriz.data.model.Post
import com.example.gestion_automotriz.repository.PostRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

data class PostUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

class PostViewModel(
    private val repository: PostRepository
) : ViewModel() {
    var uiState = androidx.compose.runtime.mutableStateOf(PostUiState())
        private set

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true, error = null, message = null)
            try {
                val posts = repository.getPosts()
                uiState.value = uiState.value.copy(posts = posts, isLoading = false)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "No se pudieron cargar las publicaciones"
                )
            }
        }
    }

    fun createPost(title: String, body: String) {
        submitPost(null, title, body) { post -> repository.createPost(post) }
    }

    fun updatePost(id: Int, title: String, body: String) {
        submitPost(id, title, body) { post -> repository.updatePost(id, post) }
    }

    fun deletePost(id: Int) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true, error = null, message = null)
            try {
                repository.deletePost(id)
                uiState.value = uiState.value.copy(
                    posts = uiState.value.posts.filterNot { it.id == id },
                    isLoading = false,
                    message = "Publicación eliminada"
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "No se pudo eliminar la publicación"
                )
            }
        }
    }

    private fun submitPost(
        id: Int?,
        title: String,
        body: String,
        save: suspend (Post) -> Post
    ) {
        viewModelScope.launch {
            val post = Post(userId = 1, id = id, title = title.trim(), body = body.trim())
            uiState.value = uiState.value.copy(isLoading = true, error = null, message = null)
            try {
                val savedPost = save(post).let { response ->
                    if (response.id == null) response.copy(id = id) else response
                }
                val currentPosts = uiState.value.posts
                val updatedPosts = if (savedPost.id == null) {
                    currentPosts
                } else {
                    currentPosts.filterNot { it.id == savedPost.id } + savedPost
                }
                uiState.value = uiState.value.copy(
                    posts = updatedPosts,
                    isLoading = false,
                    message = "Publicación guardada"
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "No se pudo guardar la publicación"
                )
            }
        }
    }

    class Factory(private val repository: PostRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (!modelClass.isAssignableFrom(PostViewModel::class.java)) {
                throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
            }
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(repository) as T
        }
    }
}
