package kr.ac.tuk.planyourday.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        var INSTANCE: TodoDatabase? = null
        fun getAppDatabase(context: Context?): TodoDatabase? {
            INSTANCE = Room.databaseBuilder(context!!, TodoDatabase::class.java, "todo-db").allowMainThreadQueries().build()
            return INSTANCE
        }
    }
}