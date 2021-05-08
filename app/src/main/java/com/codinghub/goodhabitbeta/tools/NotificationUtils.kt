package com.codinghub.goodhabitbeta.tools

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.codinghub.goodhabitbeta.R
import com.codinghub.goodhabitbeta.receiver.SnoozeReceiver
import java.text.SimpleDateFormat
import java.util.*


// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(notificationId: Int, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    Log.d("SendNoti", "Send Notification called successfully for $notificationId")
    val notificationSound: Uri = RingtoneManager
        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val contentIntent = Intent(applicationContext, ToolsFragment::class.java)
    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        notificationId,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // TODO: Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.super_alarm_icon_tools
    )

    // TODO: Step 2.2 add snooze action
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS
    )
    val collapsedView = RemoteViews(
        applicationContext.packageName,
        R.layout.notification_collapsed_water_reminder
    )
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    collapsedView.setTextViewText(R.id.notificationDate, sdf.format(System.currentTimeMillis()))

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.egg_notification_channel_id)
    ).setSmallIcon(R.drawable.cross_icon)
        .setCustomContentView(collapsedView)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(contentPendingIntent)
        .setSound(notificationSound)
//        .setContentTitle(applicationContext
//            .getString(R.string.notification_title))
//        .setContentText(messageBody)
//
//        // TODO: Step 1.13 set content intent
//        .setContentIntent(contentPendingIntent)
//        .setAutoCancel(true)
//
//        // TODO: Step 2.1 add style to builder
//        .setLargeIcon(eggImage)
//
//        // TODO: Step 2.3 add snooze action
//        .addAction(
//            R.drawable.bulb_icon,
//            applicationContext.getString(R.string.snooze),
//            snoozePendingIntent
//        )
//
//        // TODO: Step 2.5 set priority
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
    // TODO: Step 1.4 call notify
    val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
    notify(m, builder.build())
}

// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}

class NotificationUtils {
}