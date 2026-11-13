package com.example.gestion_automotriz.repository

import com.example.gestion_automotriz.data.model.Post
import com.example.gestion_automotriz.data.remote.ApiService

class PostRepository(private val api: ApiService) {
    suspend fun getPosts(): List<Post> = api.getPosts()

    suspend fun createPost(post: Post): Post = api.createPost(post)

    suspend fun updatePost(id: Int, post: Post): Post = api.updatePost(id, post)

    suspend fun deletePost(id: Int) = api.deletePost(id)
}
