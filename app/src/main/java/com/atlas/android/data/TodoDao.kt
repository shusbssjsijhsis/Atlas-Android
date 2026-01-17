package com.atlas.android.data

import androidx.room.*
import com.atlas.android.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TodoItem)

    @Update
    suspend fun update(item: TodoItem)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteById(id: String)
}
