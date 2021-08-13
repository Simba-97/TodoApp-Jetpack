package com.example.doit.todo.data

import androidx.room.*
import com.example.doit.todo.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Query("SELECT * FROM todoTable")
    fun getAllTodos(): Flow<List<Todo>>

    @Delete
    suspend fun delete(todo: Todo)
}