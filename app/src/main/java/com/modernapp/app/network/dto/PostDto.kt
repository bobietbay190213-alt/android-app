package com.modernapp.app.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.modernapp.app.domain.model.Item

@Serializable
data class PostDto(
    @SerialName("id")
    val id: Long = 0,

    @SerialName("userId")
    val userId: Long = 1,

    @SerialName("title")
    val title: String,

    @SerialName("body")
    val body: String
)

fun PostDto.toDomain(): Item = Item(
    id = id,
    title = title,
    body = body,
    userId = userId,
    imageUrl = "https://picsum.photos/seed/$id/400/300"
)
