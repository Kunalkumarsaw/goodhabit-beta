package com.codinghub.goodhabitbeta.tools.alarm

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao  {

    @Insert
    suspend fun insert (alarmEntity :AlarmEntity)

    @Query("DELETE FROM alarmTable")
    suspend fun deleteAll()

    @Query("SELECT * FROM alarmTable ORDER BY startedTime ASC")
    fun getAlarms():Flow<List<AlarmEntity>>

    @Update
    suspend fun update(alarm: AlarmEntity)

    @Delete
    suspend fun delete(alarm: AlarmEntity)
}