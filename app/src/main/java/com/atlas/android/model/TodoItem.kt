package com.atlas.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "todos")
data class TodoItem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isDone: Boolean = false
)
