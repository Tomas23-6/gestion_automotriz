package com.example.gestion_automotriz.repository

import com.example.gestion_automotriz.data.model.Post
import com.example.gestion_automotriz.data.remote.ApiService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException

class PostRepositoryTest : FunSpec({
    val api = mockk<ApiService>()
    val repository = PostRepository(api)
    val post = Post(userId = 1, id = 1, title = "Revisión", body = "Cambiar filtro")

    test("obtiene las publicaciones desde la API") {
        coEvery { api.getPosts() } returns listOf(post)

        repository.getPosts() shouldBe listOf(post)
        coVerify(exactly = 1) { api.getPosts() }
    }

    test("crea una publicación en la API") {
        coEvery { api.createPost(post) } returns post

        repository.createPost(post) shouldBe post
        coVerify(exactly = 1) { api.createPost(post) }
    }

    test("actualiza una publicación en la API") {
        coEvery { api.updatePost(1, post) } returns post

        repository.updatePost(1, post) shouldBe post
        coVerify(exactly = 1) { api.updatePost(1, post) }
    }

    test("elimina una publicación en la API") {
        coEvery { api.deletePost(1) } returns Unit

        repository.deletePost(1)
        coVerify(exactly = 1) { api.deletePost(1) }
    }

    test("propaga el error de red al cargar publicaciones") {
        val networkError = IOException("Sin conexión")
        coEvery { api.getPosts() } throws networkError

        val result = runCatching { repository.getPosts() }

        result.exceptionOrNull() shouldBe networkError
    }
})
