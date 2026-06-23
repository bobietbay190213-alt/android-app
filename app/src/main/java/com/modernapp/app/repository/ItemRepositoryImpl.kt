package com.modernapp.app.repository

import com.modernapp.app.database.ItemDao
import com.modernapp.app.database.toDomain
import com.modernapp.app.database.toEntity
import com.modernapp.app.domain.model.Item
import com.modernapp.app.network.ApiService
import com.modernapp.app.network.dto.toDomain
import com.modernapp.app.utils.Result
import com.modernapp.app.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepositoryImpl @Inject constructor(
    private val itemDao: ItemDao,
    private val apiService: ApiService
) : ItemRepository {

    override fun getAllItems(): Flow<List<Item>> =
        itemDao.getAllItems().map { entities -> entities.map { it.toDomain() } }

    override fun searchItems(query: String): Flow<List<Item>> =
        itemDao.searchItems(query).map { entities -> entities.map { it.toDomain() } }

    override fun getFavoriteItems(): Flow<List<Item>> =
        itemDao.getFavoriteItems().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getItemById(id: Long): Result<Item> {
        return try {
            val entity = itemDao.getItemById(id)
            if (entity != null) {
                Result.Success(entity.toDomain())
            } else {
                Result.Error("Item not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error fetching item", e)
        }
    }

    override suspend fun fetchItemsFromApi(page: Int): Result<List<Item>> {
        return safeApiCall {
            val posts = apiService.getPosts(page = page)
            val items = posts.map { it.toDomain() }
            itemDao.insertItems(items.map { it.toEntity() })
            items
        }
    }

    override suspend fun addItem(item: Item): Result<Long> {
        return try {
            val id = itemDao.insertItem(item.toEntity())
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error adding item", e)
        }
    }

    override suspend fun updateItem(item: Item): Result<Unit> {
        return try {
            itemDao.updateItem(item.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error updating item", e)
        }
    }

    override suspend fun deleteItem(id: Long): Result<Unit> {
        return try {
            itemDao.deleteItemById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error deleting item", e)
        }
    }

    override suspend fun toggleFavorite(id: Long, isFavorite: Boolean): Result<Unit> {
        return try {
            itemDao.updateFavoriteStatus(id, isFavorite)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error updating favorite", e)
        }
    }

    override suspend fun deleteAllItems(): Result<Unit> {
        return try {
            itemDao.deleteAllItems()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error deleting all items", e)
        }
    }
}
