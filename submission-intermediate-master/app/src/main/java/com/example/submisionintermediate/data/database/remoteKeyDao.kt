package com.example.submisionintermediate.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface remoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<remoteKey>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemote(id: String): remoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemote()

}