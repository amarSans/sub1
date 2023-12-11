package com.example.submisionintermediate.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submisionintermediate.data.response.ListStoryItem

@Dao
interface storyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<storyItem>)

    @Query("SELECT * FROM cerita")
    fun getALlStories(): PagingSource<Int, storyItem>

    @Query("SELECT * FROM cerita")
    fun getAllListStories(): List<storyItem>

    @Query("DELETE FROM cerita")
    suspend fun deleteAll()
}