package com.codinghub.goodhabitbeta.tools.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codinghub.goodhabitbeta.Constants
import com.codinghub.goodhabitbeta.receiver.AlarmReceiver
import java.util.*


@Entity(tableName = "alarmTable")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    @Nullable
    var alarmId: Int,
    var hour: Int,
    var minute: Int,

    var alarmLabel: String,

    var alarmMission: Int,

    var isStarted: Boolean,
    var startedTime: Long,

    var recurring: Boolean,
    var sunday: Boolean,
    var monday: Boolean,
    var tuesday: Boolean,
    var wednesday: Boolean,
    var thursday: Boolean,
    var friday: Boolean,
    var saturday: Boolean,
    var isSnooze: Boolean,
    var volumeLevel: Int,
    var isVibrating: Boolean
) {
    //var typingTimesMission :Int = 0


    @JvmName("setAlarmId1")
    fun setAlarmId(alarmId: Int) {
        this.alarmId = alarmId
    }

    fun getTitle(): String {
        return alarmLabel
    }

    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(Constants().RECURRING, recurring)
        intent.putExtra(Constants().MONDAY, monday)
        intent.putExtra(Constants().TUESDAY, tuesday)
        intent.putExtra(Constants().WEDNESDAY, wednesday)
        intent.putExtra(Constants().THURSDAY, thursday)
        intent.putExtra(Constants().FRIDAY, friday)
        intent.putExtra(Constants().SATURDAY, saturday)
        intent.putExtra(Constants().SUNDAY, sunday)

        intent.putExtra(Constants().TITLE, alarmLabel)

        val alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        // if alarm time has already passed, increment day by 1
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar[Calendar.DAY_OF_MONTH] = calendar[Calendar.DAY_OF_MONTH] + 1
        }
        if (!recurring) {
            Toast.makeText(
                context,
                "One time alarm scheduled for $alarmLabel  at $hour : $minute",
                Toast.LENGTH_LONG
            ).show()
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmPendingIntent
            )
        } else {
            Toast.makeText(
                context,
                "Recurring alarm ${getRecurringDays()} scheduled for $alarmLabel  at $hour : $minute",
                Toast.LENGTH_LONG
            ).show()

            val runDaily :Long= 24 * 60 * 60 * 1000L;
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                runDaily,
                alarmPendingIntent
            );
        }
        isStarted = true
    }

    fun  cancelAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)
        alarmManager.cancel(alarmPendingIntent)
        isStarted = false

        val toastText =
            String.format("Alarm cancelled for %02d:%02d with id %d", hour, minute, alarmId)
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        Log.i("cancel", toastText)
    }

    fun getRecurringDays(): String? {

        if (!recurring) {
            return null
        }
        var days: String = ""
        if (sunday && !monday && !tuesday && !wednesday && !thursday && !friday && saturday) {
            return "Week ends"
        } else if (!sunday && monday && tuesday && wednesday && thursday && friday && !saturday) {
            return "Week days"
        } else {
            if (monday) {
                days += "Mo "
            }
            if (tuesday) {
                days += "Tu "
            }
            if (wednesday) {
                days += "We "
            }
            if (thursday) {
                days += "Th "
            }
            if (friday) {
                days += "Fr "
            }
            if (saturday) {
                days += "Sa "
            }
            if (sunday) {
                days += "Su "
            }
            return days
        }

    }

    fun getNewAlarmId(): Int {
        return Random().nextInt(Int.MAX_VALUE)
    }

}