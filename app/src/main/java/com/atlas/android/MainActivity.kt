package com.atlas.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.atlas.android.data.AppDatabase
import com.atlas.android.data.TodoRepository
import com.atlas.android.ui.TodoScreen
import com.atlas.android.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. 初始化数据库
        val database = AppDatabase.getDatabase(this)
        // 2. 初始化仓库
        val repository = TodoRepository(database.todoDao())

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 3. 获取 ViewModel (注入仓库)
                    val viewModel: TodoViewModel = viewModel(
                        factory = TodoViewModel.provideFactory(repository)
                    )
                    TodoScreen(viewModel)
                }
            }
        }
    }
}
