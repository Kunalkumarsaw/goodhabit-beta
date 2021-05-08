package com.codinghub.goodhabitbeta

import android.app.Application
import com.codinghub.goodhabitbeta.tools.alarm.AlarmRepository
import com.codinghub.goodhabitbeta.tools.alarm.AlarmRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AlarmApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AlarmRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { AlarmRepository(database.alarmDao()) }
}