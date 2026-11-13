package com.example.gestion_automotriz.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("userId") val userId: Int,
    val id: Int? = null,
    val title: String,
    val body: String
)
