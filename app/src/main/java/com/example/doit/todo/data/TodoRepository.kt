package com.example.doit.todo.data

import com.example.doit.todo.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(private val dao: TodoDao) {

    suspend fun insert(todo: Todo) = withContext(Dispatchers.IO){
        dao.insert(todo)
    }

    suspend fun delete(todo: Todo) = withContext(Dispatchers.IO){
        dao.delete(todo)
    }

    fun getAllTodos(): Flow<List<Todo>> = dao.getAllTodos()
}