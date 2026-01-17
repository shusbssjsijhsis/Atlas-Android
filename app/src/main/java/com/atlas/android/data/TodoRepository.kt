package com.atlas.android.data

import com.atlas.android.model.TodoItem
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    val allTodos: Flow<List<TodoItem>> = todoDao.getAllTodos()

    suspend fun add(text: String) {
        if (text.isBlank()) return
        todoDao.insert(TodoItem(text = text))
    }

    suspend fun toggle(item: TodoItem) {
        todoDao.update(item.copy(isDone = !item.isDone))
    }

    suspend fun remove(id: String) {
        todoDao.deleteById(id)
    }
}
