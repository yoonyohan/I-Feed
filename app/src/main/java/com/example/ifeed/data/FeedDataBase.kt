package com.example.ifeed.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [User::class, Post::class],
    version = 1,
    exportSchema = false
)
abstract class FeedDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: FeedDataBase? = null

        fun getDataBase(context: Context): FeedDataBase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, FeedDataBase::class.java, "feed_database")
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}