package com.modernapp.app.repository

import com.modernapp.app.domain.model.Item
import com.modernapp.app.utils.Result
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getAllItems(): Flow<List<Item>>
    fun searchItems(query: String): Flow<List<Item>>
    fun getFavoriteItems(): Flow<List<Item>>
    suspend fun getItemById(id: Long): Result<Item>
    suspend fun fetchItemsFromApi(page: Int = 1): Result<List<Item>>
    suspend fun addItem(item: Item): Result<Long>
    suspend fun updateItem(item: Item): Result<Unit>
    suspend fun deleteItem(id: Long): Result<Unit>
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean): Result<Unit>
    suspend fun deleteAllItems(): Result<Unit>
}
