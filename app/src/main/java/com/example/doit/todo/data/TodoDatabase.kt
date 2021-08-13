package com.example.doit.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.doit.todo.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun getDao() : TodoDao
}