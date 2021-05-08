package com.codinghub.goodhabitbeta.receiver

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import com.codinghub.goodhabitbeta.Constants
import com.codinghub.goodhabitbeta.tools.alarm.AlarmDismiss


class AlarmService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    override fun onCreate() {
        super.onCreate()
        var alarmUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        // play ringtone
        mediaPlayer = MediaPlayer.create(applicationContext, alarmUri)
        mediaPlayer!!.isLooping = true


        // play Vibrator

        vibrator =  getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intentDismiss = Intent(this, AlarmDismiss::class.java)
        intentDismiss.putExtra(Constants().TITLE, intent!!.getStringExtra(Constants().TITLE))
        mediaPlayer!!.start()

        if (Build.VERSION.SDK_INT >= 26) {
            vibrator!!.vibrate(VibrationEffect.createOneShot(20000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator!!.vibrate(20000)
        }
        intentDismiss.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentDismiss)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
        vibrator!!.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}