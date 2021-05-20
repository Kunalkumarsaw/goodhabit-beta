package com.codinghub.goodhabitbeta.tools.alarm

import android.util.Log
import androidx.lifecycle.*
import com.codinghub.goodhabitbeta.R
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class AlarmHomeViewModel(private val repository: AlarmRepository) : ViewModel(), LifecycleObserver {


    var hour: Int = 0
    var minute = 0
    var alarmLabel = ""

    var alarmMission: Int = 0

    var isStarted = false
    var startedTime: Long = 0L

    var recurring: Boolean = true
    var volumeLevel: Int = 80
    var isVibrating: Boolean = false
    var isAlarmSnooze = false
    var daysArrayList = arrayListOf(false, true, true, true, true, true, false)
    var monday = false
    var tuesday = true
    var wednesday = true
    var thursday = true
    var friday = true
    var saturday = true
    var sunday = false


    //    var labelObserver : MutableLiveData<String> get() = alarmLabel
    // Mission Status
    private val labelObserver = MutableLiveData("fucked!")

    fun getLabel(): LiveData<String> = labelObserver
    fun setLabel(value: String) {
        labelObserver.postValue(value)
    }

    // Selected Mission
    val selectedMission = arrayOf(
        true,
        false,
        false,
        false,
        false,
        false
    )
    //Mission fragment icons
    val missionIcons = arrayOf(
        R.drawable.alarm_icon,
        R.drawable.keyboard_icon,
        R.drawable.memory_icon,
        R.drawable.shake_icon,
        R.drawable.math_icon,
        R.drawable.barcode_icon,
    )
    val missionTitle = arrayOf("Off", "Typing", "Memory", "Shake", "Math", "Barcode")
    val missionDescription = arrayOf(
        "",
        "Motivate yourself with inspiration phrases",
        "Get our day rolling with fun game",
        "Shake your arms hard",
        "Stimulate your brain over and over ",
        "Place the target as far as possible"
    )


    private val itemFeaturesStatus = MutableLiveData(arrayListOf("off", "off", "off", "None!"))
    val getFeatureStatus: LiveData<ArrayList<String>> get() = itemFeaturesStatus
    fun updateFeatureStatus(value: ArrayList<String>) {
        itemFeaturesStatus.postValue(value)
    }
    // val allAlarms : LiveData<List<AlarmEntity>> = repository.allAlarms.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(alarmEntity: AlarmEntity) = viewModelScope.launch {
        repository.insert(alarmEntity)
    }


    init {
        Log.i("AlarmHomeViewModel", "AlarmHomeViewModel created!")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, 1)
        minute = calendar.get(Calendar.MINUTE)
        hour = calendar.get(Calendar.HOUR_OF_DAY)

    }

    class AlarmViewModelFactory(private val repository: AlarmRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlarmHomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlarmHomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}