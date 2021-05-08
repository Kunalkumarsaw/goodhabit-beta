package com.codinghub.goodhabitbeta.tools.alarm

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class AlarmRepository( private val alarmDao: AlarmDao) {

    val allAlarms : Flow<List<AlarmEntity>> = alarmDao.getAlarms()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(alarm:AlarmEntity){
        alarmDao.insert(alarm)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(alarm:AlarmEntity){
        alarmDao.update(alarm)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(alarm:AlarmEntity){
        alarmDao.delete(alarm)
    }
}