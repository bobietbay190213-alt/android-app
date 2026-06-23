package com.modernapp.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.modernapp.app.domain.model.Item

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "body")
    val body: String,

    @ColumnInfo(name = "user_id")
    val userId: Long = 1,

    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

fun ItemEntity.toDomain(): Item = Item(
    id = id,
    title = title,
    body = body,
    userId = userId,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
    createdAt = createdAt
)

fun Item.toEntity(): ItemEntity = ItemEntity(
    id = id,
    title = title,
    body = body,
    userId = userId,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
    createdAt = createdAt
)
