package com.example.gestion_automotriz.viewmodel

import com.example.gestion_automotriz.data.model.Post
import com.example.gestion_automotriz.repository.PostRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest : FunSpec({
    lateinit var repository: PostRepository
    lateinit var viewModel: PostViewModel
    lateinit var dispatcher: TestDispatcher
    val initialPost = Post(userId = 1, id = 1, title = "Mantención", body = "Aceite")

    beforeTest {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        coEvery { repository.getPosts() } returns listOf(initialPost)
        viewModel = PostViewModel(repository)
        dispatcher.scheduler.advanceUntilIdle()
    }

    afterTest {
        Dispatchers.resetMain()
    }

    test("carga publicaciones y finaliza el estado de carga") {
        viewModel.uiState.value.posts shouldBe listOf(initialPost)
        viewModel.uiState.value.isLoading shouldBe false
    }

    test("crea una publicación y la agrega a la lista") {
        val created = Post(userId = 1, id = 2, title = "Batería", body = "Reemplazo")
        coEvery {
            repository.createPost(Post(userId = 1, title = "Batería", body = "Reemplazo"))
        } returns created

        viewModel.createPost(" Batería ", " Reemplazo ")
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.value.posts shouldBe listOf(initialPost, created)
        viewModel.uiState.value.message shouldBe "Publicación guardada"
    }

    test("actualiza una publicación existente") {
        val updated = initialPost.copy(title = "Mantención anual")
        coEvery {
            repository.updatePost(1, Post(userId = 1, id = 1, title = "Mantención anual", body = "Aceite"))
        } returns updated

        viewModel.updatePost(1, "Mantención anual", "Aceite")
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.value.posts shouldBe listOf(updated)
    }

    test("elimina una publicación existente") {
        coEvery { repository.deletePost(1) } returns Unit

        viewModel.deletePost(1)
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.value.posts shouldBe emptyList()
        viewModel.uiState.value.message shouldBe "Publicación eliminada"
    }

    test("muestra un error de red y permite reintentar la carga") {
        coEvery { repository.getPosts() } throws IOException("Sin conexión")

        viewModel.loadPosts()
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.value.isLoading shouldBe false
        viewModel.uiState.value.error shouldBe "Sin conexión"
        viewModel.uiState.value.posts shouldBe listOf(initialPost)
    }
})
