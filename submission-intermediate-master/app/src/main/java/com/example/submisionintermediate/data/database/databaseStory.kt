package com.example.submisionintermediate.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [storyItem::class, remoteKey::class],
    version = 2,
    exportSchema = false
)
abstract class databaseStory :RoomDatabase(){
    abstract fun getListStoryDao():storyDao
    abstract fun getKey():remoteKeyDao
    companion object {
        @Volatile
        private var INSTANCE: databaseStory? = null

        @JvmStatic
        fun getDatabase(context: Context): databaseStory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    databaseStory::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}