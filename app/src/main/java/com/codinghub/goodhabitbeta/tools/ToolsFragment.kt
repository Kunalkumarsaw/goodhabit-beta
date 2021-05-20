package com.codinghub.goodhabitbeta.tools

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinghub.goodhabitbeta.AlarmApplication
import com.codinghub.goodhabitbeta.R
import com.codinghub.goodhabitbeta.databinding.ToolsFragmentBinding
import com.codinghub.goodhabitbeta.receiver.AlarmReceiver
import com.codinghub.goodhabitbeta.tools.alarm.AlarmEntity
import com.codinghub.goodhabitbeta.tools.alarm.AlarmHome
import kotlinx.android.synthetic.main.reminder_bottom_sheet.*
import kotlinx.android.synthetic.main.tools_fragment.*
import kotlinx.android.synthetic.main.water_reminder_layout.*
import java.util.*

class ToolsFragment : Fragment(), OnItemClickListener, AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance() = ToolsFragment()
    }


    // View Model
    private val viewModel: ToolsViewModel by viewModels {
        ToolsViewModel.ToolsViewModelFactory(
            (activity?.application as AlarmApplication).repository
        )
    }
    private lateinit var binding: ToolsFragmentBinding
    private var running = false
    private var pauseOffset: Long = 0
    var timeLapse: MutableList<String> = mutableListOf()

    // Water Reminder
    var startTimeDefaultReminder = 2
    var endTimeDefaultReminder = 10
    var gapTimeDefaultReminder = 1
    var goalWaterReminder = 3000
    private val REQUEST_CODE = 0
    lateinit var notifyPendingIntent: PendingIntent

    // Upcoming alarms
    lateinit var menuAlarmEntity: AlarmEntity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ToolsFragmentBinding.inflate(layoutInflater)
        binding.toolsViewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Upcoming Alarms
        binding.apply {

            recyclerUpcomingAlarms.layoutManager = LinearLayoutManager(context)
            viewModel.allAlarms.observe(viewLifecycleOwner, { alarms ->
                recyclerUpcomingAlarms.adapter =
                    UpcomingAlarmRecyclerAdapter(alarms, this@ToolsFragment)
            })
        }

        // Super Alarm
        binding.smartAlarmCardTools.setOnClickListener {
            val intent = Intent(context, AlarmHome::class.java)
            startActivity(intent)
        }


        // Stop Watch
        textClockTools.format24Hour = "h:mm a"

        stopWatchCardTools.setOnClickListener {
            tools_pressed_card.visibility = (View.GONE)
            stopWatchLayoutTools.visibility = View.VISIBLE
            resetPlayLapseCard.visibility = View.VISIBLE
        }
        cross_button_stopwatch.setOnClickListener {
            tools_pressed_card.visibility = (View.VISIBLE)
            stopWatchLayoutTools.visibility = View.GONE
            resetPlayLapseCard.visibility = View.GONE
        }
        chronometer_stopwatch.base = SystemClock.elapsedRealtime()
        play_button_stopwatch.setOnClickListener {
            startChronometer()
        }
        resetStopWatchButton.setOnClickListener {
            resetChronometer()
        }
        lapseStopWatchButton.setOnClickListener {
            lapseChronometer()
        }
        // Water Reminder
        setDefaultButton.setOnClickListener {
            val name = CupSizeBottomSheetDialogFragment(sheetType = 0)
            name.show(childFragmentManager, "cupSizeSheet")
            Log.d("ToolsDebug", name.getCupSize().toString())
        }
        reminderButton.setOnClickListener {
            openReminderSheet()
        }
        waterReminderCardTools.setOnClickListener {
            tools_pressed_card.visibility = (View.GONE)
            waterReminderIncludeLayoutTools.visibility = View.VISIBLE
        }
        cross_button_water_reminder.setOnClickListener {
            tools_pressed_card.visibility = (View.VISIBLE)
            waterReminderIncludeLayoutTools.visibility = View.GONE
            setDefaultReminder()
        }
        createChannel(
            getString(R.string.egg_notification_channel_id),
            getString(R.string.egg_notification_channel_name)
        )

        // Time Tracking

        timeTrackingCardTools.setOnClickListener {
            val intent = Intent(context, TimeTracking::class.java)
            startActivity(intent)
        }
    }

    // water Reminder
    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )// TODO: Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                getString(R.string.breakfast_notification_channel_description)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
        // TODO: Step 1.6 END create a channel
    }

    private fun openReminderSheet() {
        val name = CupSizeBottomSheetDialogFragment(sheetType = 1)
        name.show(childFragmentManager, "ReminderSheet")
    }

    private fun setDefaultReminder() {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        val selectedInterval = 30000L
        val triggerTime = SystemClock.elapsedRealtime().plus(selectedInterval)
        notifyIntent.putExtra("notificationId", 1)
        val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            notifyIntent,
            0
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            60000L,
            notifyPendingIntent
        )
    }

    // Stop watch
    private fun startChronometer() {
        if (!running) {
            chronometer_stopwatch.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer_stopwatch.start()
            running = true
            play_button_stopwatch.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            chronometer_stopwatch.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer_stopwatch.base;
            running = false;
            play_button_stopwatch.setImageResource(R.drawable.play_arrow)
        }
    }

    private fun stopChronometer() {
        if (running) {
            chronometer_stopwatch.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chronometer_stopwatch.base
            running = false
            play_button_stopwatch.setImageResource(R.drawable.play_arrow)
        }
    }

    private fun lapseChronometer() {
        if (running) {
            timeLapse.add((SystemClock.elapsedRealtime() - chronometer_stopwatch.base).toString())
            val arrayAdapter = StopWatchCustomAdapter(requireContext(), timeLapse)
            listViewLapsedStopwatch.adapter = arrayAdapter
        }
    }

    fun resetChronometer() {
        chronometer_stopwatch.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
        stopChronometer()
        timeLapse.clear()
        val arrayAdapter2 = StopWatchCustomAdapter(requireContext(), timeLapse)
        listViewLapsedStopwatch.adapter = arrayAdapter2
    }

    override fun onItemClicked(alarm: AlarmEntity) {
        Toast.makeText(context, "Item Clicked!", Toast.LENGTH_LONG).show()
    }

    override fun onRadioButtonClicked(alarm: AlarmEntity) {
        viewModel.update(alarm)
        if (alarm.isStarted){
            alarm.schedule(requireContext())
        }else{
            alarm.cancelAlarm(context = requireContext())
        }
    }

    override fun onDropDownClicked(spinner: Spinner, alarm: AlarmEntity) {
        menuAlarmEntity = alarm
        // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.upcomingAlarmsMenu,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        spinner.setBackgroundColor(
            ResourcesCompat.getColor(
                resources,
                R.color.background,
                null
            )
        )
        spinner.onItemSelectedListener = this
        spinner.performClick()

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (position) {
            0 -> {
                Toast.makeText(context, "You touched $position", Toast.LENGTH_LONG).show()
            }
            1 -> {
                menuAlarmEntity.alarmId = menuAlarmEntity.getNewAlarmId()
                menuAlarmEntity.startedTime = menuAlarmEntity.startedTime + 10L
                menuAlarmEntity.schedule(requireContext())
                viewModel.insert(menuAlarmEntity)
            }
            2 -> {
                menuAlarmEntity.cancelAlarm(requireContext())
                viewModel.delete(menuAlarmEntity)
            }
            else -> {
                Toast.makeText(context, "You touched $position  and else", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(context, "You touched nothing!", Toast.LENGTH_LONG).show()

    }


}