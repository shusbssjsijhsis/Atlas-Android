package com.atlas.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.atlas.android.data.TodoRepository
import com.atlas.android.model.TodoItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    // 从数据库 Flow 转换为 StateFlow
    val uiState: StateFlow<List<TodoItem>> = repository.allTodos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTodo(text: String) = viewModelScope.launch {
        repository.add(text)
    }

    fun toggleTodo(id: String) = viewModelScope.launch {
        // 我们需要找到当前的 item，这在真实 app 中通常由 repo 提供单项查询
        // 这里简化：直接在 UI 层传整个 item 进来，或者在 ViewModel 里遍历
        // 为了简单，我们让 UI 层传 item，这里稍微改下签名，或者我们在 UI 层处理
    }
    
    // 为了配合之前的 UI 逻辑，我们这里做个变通
    fun toggleTodoItem(item: TodoItem) = viewModelScope.launch {
        repository.toggle(item)
    }

    fun removeTodo(id: String) = viewModelScope.launch {
        repository.remove(id)
    }

    // 定义 Factory 以便在 MainActivity 中注入依赖
    companion object {
        fun provideFactory(repository: TodoRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TodoViewModel(repository)
            }
        }
    }
}
