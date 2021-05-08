package com.codinghub.goodhabitbeta.tools.alarm

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.util.*

class AlarmHomeViewModel(private val repository: AlarmRepository) : ViewModel() {


    var hour :Int = 0
    var minute =0
    var alarmLabel =""

    var alarmMission: Int =0

    var isStarted  = false
    var startedTime: Long = 0L

    var recurring : Boolean = true
    var volumeLevel: Int = 80
    var isVibrating :Boolean = false
    var isAlarmSnooze = false
    var daysArrayList = arrayListOf(false, true, true, true, true, true, false)
    var monday = false
    var tuesday = true
    var wednesday = true
    var thursday = true
    var friday = true
    var saturday = true
    var sunday = false



    // Mission Status
    var itemFeaturesStatus = arrayListOf("off", "off", "off", "off")

    val allAlarms : LiveData<List<AlarmEntity>> = repository.allAlarms.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert( alarmEntity: AlarmEntity)= viewModelScope.launch {
        repository.insert(alarmEntity)
    }


    init {
        Log.i("AlarmHomeViewModel", "AlarmHomeViewModel created!")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE,1)
        minute = calendar.get(Calendar.MINUTE)
        hour = calendar.get(Calendar.HOUR_OF_DAY)

    }
    class AlarmViewModelFactory( private val repository: AlarmRepository ):ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlarmHomeViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return AlarmHomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}