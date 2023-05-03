package com.example.todolist.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.model.dao.RootTaskDao
import com.example.todolist.model.entity.RootTaskData

@Database(entities = [RootTaskData::class], version = 1, exportSchema = false)
abstract class RootTaskDatabase : RoomDatabase() {
    abstract fun rootTaskDao(): RootTaskDao

    companion object {
        private const val DB_NAME = "todo_list.db"

        @Volatile
        private var instance: RootTaskDatabase? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    RootTaskDatabase::class.java,
                    DB_NAME)
                    .build()
            }
    }
}