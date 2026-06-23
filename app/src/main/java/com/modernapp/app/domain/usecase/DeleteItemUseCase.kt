package com.modernapp.app.domain.usecase

import com.modernapp.app.repository.ItemRepository
import com.modernapp.app.utils.Result
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deleteItem(id)

    suspend fun deleteAll(): Result<Unit> = repository.deleteAllItems()

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean): Result<Unit> =
        repository.toggleFavorite(id, isFavorite)
}
