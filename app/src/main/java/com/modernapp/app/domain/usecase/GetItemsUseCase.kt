package com.modernapp.app.domain.usecase

import com.modernapp.app.domain.model.Item
import com.modernapp.app.repository.ItemRepository
import com.modernapp.app.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    fun getAllItems(): Flow<List<Item>> = repository.getAllItems()

    fun searchItems(query: String): Flow<List<Item>> =
        if (query.isBlank()) repository.getAllItems()
        else repository.searchItems(query)

    fun getFavoriteItems(): Flow<List<Item>> = repository.getFavoriteItems()

    suspend fun getItemById(id: Long): Result<Item> = repository.getItemById(id)

    suspend fun fetchFromApi(page: Int = 1): Result<List<Item>> =
        repository.fetchItemsFromApi(page)
}
