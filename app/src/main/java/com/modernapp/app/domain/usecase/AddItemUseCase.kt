package com.modernapp.app.domain.usecase

import com.modernapp.app.domain.model.Item
import com.modernapp.app.repository.ItemRepository
import com.modernapp.app.utils.Result
import javax.inject.Inject

class AddItemUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(title: String, body: String): Result<Long> {
        if (title.isBlank()) {
            return Result.Error("Title cannot be empty")
        }
        if (body.isBlank()) {
            return Result.Error("Body cannot be empty")
        }
        val item = Item(
            title = title.trim(),
            body = body.trim()
        )
        return repository.addItem(item)
    }

    suspend fun update(item: Item): Result<Unit> {
        if (item.title.isBlank()) {
            return Result.Error("Title cannot be empty")
        }
        return repository.updateItem(item)
    }
}
