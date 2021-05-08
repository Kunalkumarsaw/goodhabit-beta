package com.codinghub.goodhabitbeta.tools.alarm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [AlarmEntity::class], version = 1, exportSchema = false)
abstract class AlarmRoomDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    private class AlarmDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val alarmDao = database.alarmDao()

                    // Delete all content here
                    alarmDao.deleteAll()

                    // Add sample words
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = System.currentTimeMillis()
                    calendar.add(Calendar.MINUTE, 10)
                    val alarmId = Random().nextInt(Int.MAX_VALUE)
                    var alarm = AlarmEntity(
                        alarmId,
                        hour = 4,
                        minute = 20,

                        alarmMission = 0,
                        alarmLabel = "Test alarm 1",
                        recurring = false,
                        isSnooze = true,
                        volumeLevel = 50,
                        startedTime = calendar.timeInMillis - 10 * 60 * 1000,

                        monday = false,
                        tuesday = true,
                        wednesday = true,
                        thursday = true,
                        friday = true,
                        saturday = true,
                        sunday = false,

                        isStarted = true,
                        isVibrating = false
                    )
                    alarmDao.insert(alarm)
                    calendar.add(Calendar.MINUTE, 5)
                    val alarmId2 = Random().nextInt(Int.MAX_VALUE)
                    alarm = AlarmEntity(
                        alarmId2,
                        hour = 5,
                        minute = 55,
                        alarmMission = 0,
                        alarmLabel = "Test alarm 2",
                        recurring = false,
                        isSnooze = true,
                        volumeLevel = 50,
                        startedTime = calendar.timeInMillis - 15 * 60 * 1000,
                        monday = false,
                        tuesday = true,
                        wednesday = true,
                        thursday = true,
                        friday = true,
                        saturday = true,
                        sunday = false,
                        isVibrating = true,
                        isStarted = false
                    )
                    alarmDao.insert(alarm)


                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: AlarmRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AlarmRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmRoomDatabase::class.java,
                    "alarm_database"
                )
                    .addCallback(AlarmDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}