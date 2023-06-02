package com.primagiant.storyapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.primagiant.storyapp.data.local.entity.RemoteKey
import com.primagiant.storyapp.data.remote.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeyDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java, "db_story"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}