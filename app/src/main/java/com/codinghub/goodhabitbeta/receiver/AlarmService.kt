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
import android.util.Log
import com.codinghub.goodhabitbeta.Constants
import com.codinghub.goodhabitbeta.MainActivity
import com.codinghub.goodhabitbeta.tools.ToolsFragment
import com.codinghub.goodhabitbeta.tools.alarm.AlarmDismiss


class AlarmService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    override fun onCreate() {
        super.onCreate()
        Log.d("alarm","On create method of service called!")
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
        Log.d("alarm","On Start command method of service called!")

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
        Log.d("alarm","On Destroy method of service called!")
        mediaPlayer!!.stop()
        vibrator!!.cancel()
        val intentToolsFragment = Intent(this, MainActivity::class.java)
        intentToolsFragment.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentToolsFragment)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}