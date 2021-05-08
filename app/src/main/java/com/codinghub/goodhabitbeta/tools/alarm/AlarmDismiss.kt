package com.codinghub.goodhabitbeta.tools.alarm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codinghub.goodhabitbeta.Constants
import com.codinghub.goodhabitbeta.databinding.ActivityAlarmDismissBinding
import com.codinghub.goodhabitbeta.receiver.AlarmService


class AlarmDismiss : AppCompatActivity() {

    lateinit var binding :ActivityAlarmDismissBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmDismissBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val label = intent.getStringExtra(Constants().TITLE)

        binding.apply {

            labelDismiss.text = label

            dismissButtonDismiss.setOnClickListener {
                val intentService = Intent(applicationContext, AlarmService::class.java)
                applicationContext.stopService(intentService)
                finish()
            }
        }
    }
}