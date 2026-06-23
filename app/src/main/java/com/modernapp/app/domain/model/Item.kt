package com.modernapp.app.domain.model

data class Item(
    val id: Long = 0,
    val title: String,
    val body: String,
    val userId: Long = 1,
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
