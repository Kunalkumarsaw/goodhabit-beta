package com.codinghub.goodhabitbeta.tools.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.codinghub.goodhabitbeta.AlarmApplication
import com.codinghub.goodhabitbeta.R
import com.codinghub.goodhabitbeta.databinding.ActivityAlarmHomeBinding
import com.codinghub.goodhabitbeta.databinding.ItemFeatureAlarmHomeBinding
import com.codinghub.goodhabitbeta.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.activity_alarm_home.*
import soup.neumorphism.NeumorphButton
import soup.neumorphism.ShapeType
import java.util.*
import kotlin.collections.ArrayList

class AlarmHome : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmHomeBinding

    // Days
    private var daysButtonRoot = ArrayList<Button>(7)
    private val daysLetters = arrayListOf("S", "M", "T", "W", "T", "F", "S")

    // Features
    private var itemFeaturesRoot = ArrayList<ItemFeatureAlarmHomeBinding>(4)
    private var itemFeaturesTitle = arrayListOf(
        "Mission",
        "Alarm Power up",
        "Wake up check",
        "Label"
    )

    // View Model
    private val viewModel: AlarmHomeViewModel by viewModels {
        AlarmHomeViewModel.AlarmViewModelFactory(
            (application as AlarmApplication).repository
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmHomeBinding.inflate(layoutInflater)
        lifecycle.addObserver(viewModel)

        setContentView(binding.root)

        // Initializing
        binding.apply {
            // Time picker set up
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePickerAlarmHome.minute = viewModel.minute
                timePickerAlarmHome.hour = viewModel.hour
            } else {
                timePickerAlarmHome.currentMinute = viewModel.minute
                timePickerAlarmHome.currentHour = viewModel.hour
            }
            timePickerAlarmHome.setIs24HourView(true)
            timePickerAlarmHome.setOnTimeChangedListener { view, hourOfDay, minute ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    view.minute = minute
                    view.hour = hourOfDay
                } else {
                    view.currentMinute = minute
                    view.currentHour = hourOfDay
                }
                viewModel.hour = hourOfDay
                viewModel.minute = minute
            }
            // Back Pressed
            backButtonAlarmHome.setOnClickListener {
                onBackPressed()
            }

            // Selecting days
            daysButtonRoot = arrayListOf(
                daySundayAlarmHome.root,
                dayMondayAlarmHome.root,
                dayTuesdayAlarmHome.root,
                dayWednesdayAlarmHome.root,
                dayThursdayAlarmHome.root,
                dayFridayAlarmHome.root,
                daySaturdayAlarmHome.root
            )
            daysButtonRoot.forEachIndexed { index, button ->
                button.text = daysLetters[index]
                updateDaysButton(index, button)
                button.setOnClickListener {
                    viewModel.daysArrayList[index] = !viewModel.daysArrayList[index]
                    updateDaysButton(index, button)
                    viewModel.recurring = isRecurring(viewModel.daysArrayList)
                }
            }
            // Initialization of Mission / Power up / Wake up check
            itemFeaturesRoot = arrayListOf(
                includeMissionAlarmHome,
                includePowerUpAlarmHome,
                includeWakeupCheckAlarmHome,
                includeLabelAlarmHome
            )
            itemFeaturesRoot.forEachIndexed { index, itemFeatureAlarmHomeBinding ->

                itemFeatureAlarmHomeBinding.titleItemFeatureAlarmHome.text =
                    itemFeaturesTitle[index]
                when(index){
                    0-> {
                        itemFeatureAlarmHomeBinding.itemFeatureButtonAlarmHome.setOnClickListener {
                            val bottomSheet =  AlarmBottomSheetFragment(0, viewModel)
                            bottomSheet.show(supportFragmentManager,"MissionSheet")
                        }
                    }
                    1->{

                    }
                    2->{
                        itemFeatureAlarmHomeBinding.itemFeatureButtonAlarmHome.setOnClickListener {
                            val bottomSheet =  AlarmBottomSheetFragment(9, viewModel)
                            bottomSheet.show(supportFragmentManager,"WakeupSheet")
                        }
                    }
                    3->{
                        itemFeatureAlarmHomeBinding.itemFeatureButtonAlarmHome.setOnClickListener {
                            val bottomSheet =  AlarmBottomSheetFragment(10, viewModel)
                            bottomSheet.show(supportFragmentManager,"AlarmSheet")
                        }

                    }
                }
                itemFeatureAlarmHomeBinding.typeItemFeatureAlarmHome.text =
                    viewModel.getFeatureStatus.value!![index]
            }
//            viewModel.getLabel().observe(this@AlarmHome,{ value ->
//                includeLabelAlarmHome.typeItemFeatureAlarmHome.text = value
//                Log.d("label", " New Observer called successfully with $value")
//            })

            viewModel.getFeatureStatus.observe(this@AlarmHome, {
                itemFeaturesRoot.forEachIndexed { index, itemFeatureAlarmHomeBinding ->
                    itemFeatureAlarmHomeBinding.typeItemFeatureAlarmHome.text = it[index]
                }
                Log.d("label", "Observer called successfully with ${it[3]}")
            })

            // Volume SeeK Bar
            volumeSeekBarAlarmHome.progress = 60
            volumeTextAlarmHome.text= volumeSeekBarAlarmHome.progress.toString().plus("%")
            volumeSeekBarAlarmHome.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    volumeTextAlarmHome.text = progress.toString().plus("%")
                    viewModel.volumeLevel = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

            // Snooze
            updateSnoozeButton()
            onSnoozeAlarmHome.setOnClickListener {
                viewModel.isAlarmSnooze = !viewModel.isAlarmSnooze
                updateSnoozeButton()
            }
            offSnoozeAlarmHome.setOnClickListener {
                viewModel.isAlarmSnooze = !viewModel.isAlarmSnooze
                updateSnoozeButton()
            }

            // Save the Alarm
            saveButtonAlarmHome.setOnClickListener {
                viewModel.startedTime = timePickerAlarmHome.drawingTime
                val alarmId = Random().nextInt(Int.MAX_VALUE)
                val alarm = AlarmEntity(
                    alarmId,
                    viewModel.hour,
                    viewModel.minute,
                    viewModel.alarmLabel,
                    viewModel.alarmMission,
                    viewModel.isStarted,
                    viewModel.startedTime,
                    viewModel.recurring,
                    viewModel.daysArrayList[0],
                    viewModel.daysArrayList[1],
                    viewModel.daysArrayList[2],
                    viewModel.daysArrayList[3],
                    viewModel.daysArrayList[4],
                    viewModel.daysArrayList[5],
                    viewModel.daysArrayList[6],
                    viewModel.isAlarmSnooze,
                    viewModel.volumeLevel,
                    viewModel.isVibrating
                )
                alarm.schedule(this@AlarmHome)

                viewModel.insert(alarm)
                Toast.makeText(
                    applicationContext,
                    "Alarm Got Saved Successfully!",
                    Toast.LENGTH_LONG
                ).show()
                onBackPressed()

            }

            // Handling Delete Button
            deleteButtonAlarmHome.setOnClickListener {
                onBackPressed()
            }

            // Starting the preview button
            previewButtonAlarmHome.setOnClickListener {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    calendar.set(Calendar.HOUR_OF_DAY, timePickerAlarmHome.hour)
                    calendar.set(Calendar.MINUTE, timePickerAlarmHome.minute)
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, timePickerAlarmHome.currentHour)
                    calendar.set(Calendar.MINUTE, timePickerAlarmHome.currentMinute)
                }
                setPreviewAlarm(calendar)
            }

            // Label Bottom sheet

        }
    }

    private fun isRecurring(daysArrayList: ArrayList<Boolean>): Boolean {
       return daysArrayList.contains(true)
    }

    private fun setPreviewAlarm(calendar: Calendar) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show()


    }

    private fun updateSnoozeButton() {
        if (viewModel.isAlarmSnooze) {
            showSnoozeAnim(
                neumorphButton1 = binding.onSnoozeAlarmHome,
                neumorphButton2 = binding.offSnoozeAlarmHome
            )
        } else {
            showSnoozeAnim(
                neumorphButton1 = binding.offSnoozeAlarmHome,
                neumorphButton2 = binding.onSnoozeAlarmHome
            )
        }
    }

    private fun showSnoozeAnim(neumorphButton1: NeumorphButton, neumorphButton2: NeumorphButton) {
        binding.apply {
            neumorphButton1.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
            neumorphButton1.setShapeType(ShapeType.FLAT)
            neumorphButton1.elevation = 5f
            neumorphButton1.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.purpleTheme,
                    null
                )
            )
            neumorphButton2.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.primary_text,
                    null
                )
            )
            neumorphButton2.setShapeType(ShapeType.PRESSED)
            neumorphButton2.elevation = 1f
            neumorphButton2.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.background,
                    null
                )
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    private fun updateDaysButton(index: Int, button: Button) {
        if (viewModel.daysArrayList[index]) {
            button.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.solidroundedbutton,
                null
            )
            button.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
        } else {
            button.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.strokedroundbutton,
                null
            )
            button.setTextColor(ResourcesCompat.getColor(resources, R.color.purpleTheme, null))
        }
    }
}