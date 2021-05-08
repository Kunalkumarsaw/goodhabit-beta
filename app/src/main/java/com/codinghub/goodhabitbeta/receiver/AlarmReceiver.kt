package com.codinghub.goodhabitbeta.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.codinghub.goodhabitbeta.Constants
import java.util.*


class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        Toast.makeText(context, context.getText(R.string.eggs_ready), Toast.LENGTH_SHORT).show()



        if (!intent!!.getBooleanExtra(Constants().RECURRING, false)) {
            startAlarmService(context, intent);
        } else{
            if (alarmIsToday(intent)) {
                startAlarmService(context, intent);
            }
        }

        //add call to sendNotification
//        val notificationManager = context?.let {
//            ContextCompat.getSystemService(
//                it,
//                NotificationManager::class.java
//            )
//        } as NotificationManager
//
//        val notificationId = intent?.getIntExtra("notificationId",1) as Int
//
//        notificationManager.sendNotification(
//            notificationId,
//            context
//        )

    }

    private fun startAlarmService(context: Context?, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra(Constants().TITLE, intent.getStringExtra(Constants().TITLE))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(intentService)
        } else {
            context!!.startService(intentService)
        }

    }

    private fun alarmIsToday(intent: Intent): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        when(calendar.get(Calendar.DAY_OF_WEEK)){

            Calendar.MONDAY -> {
                return intent.getBooleanExtra(Constants().MONDAY, false)
            }
            Calendar.TUESDAY -> {
                return intent.getBooleanExtra(Constants().TUESDAY, false)
            }
            Calendar.WEDNESDAY -> {
                return intent.getBooleanExtra(Constants().WEDNESDAY, false)
            }
            Calendar.THURSDAY -> {
                return intent.getBooleanExtra(Constants().THURSDAY, false)
            }
            Calendar.FRIDAY -> {
                return intent.getBooleanExtra(Constants().FRIDAY, false)
            }
            Calendar.SATURDAY -> {
                return intent.getBooleanExtra(Constants().SATURDAY, false)
            }
            Calendar.SUNDAY -> {
                return intent.getBooleanExtra(Constants().SUNDAY, false)
            }
            else ->{
                return false
            }
        }
    }
}