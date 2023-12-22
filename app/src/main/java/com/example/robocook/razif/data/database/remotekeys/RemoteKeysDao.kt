package com.example.robocook.razif.data.database.remotekeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun fetchRemoteKeysId(id: Int): RemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putAll(remoteKey: List<RemoteKeys>)

    @Query("DELETE FROM remote_keys")
    suspend fun removeRemoteKeys()

}