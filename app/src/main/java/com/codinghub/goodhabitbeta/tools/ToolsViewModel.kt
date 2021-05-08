package com.codinghub.goodhabitbeta.tools

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.codinghub.goodhabitbeta.receiver.AlarmReceiver
import com.codinghub.goodhabitbeta.tools.alarm.AlarmEntity
import com.codinghub.goodhabitbeta.tools.alarm.AlarmHomeViewModel
import com.codinghub.goodhabitbeta.tools.alarm.AlarmRepository
import kotlinx.coroutines.launch

class ToolsViewModel(private val repository: AlarmRepository) : ViewModel() {

    val allAlarms : LiveData<List<AlarmEntity>> = repository.allAlarms.asLiveData()

    // Updating a exiting alarm
    fun update( alarmEntity: AlarmEntity)= viewModelScope.launch {
        repository.update(alarmEntity)
    }
    // Deleting a exiting alarm
    fun delete( alarmEntity: AlarmEntity)= viewModelScope.launch {
        repository.delete(alarmEntity)
    }
    // Inserting a new alarm
    fun insert( alarmEntity: AlarmEntity)= viewModelScope.launch {
        repository.insert(alarmEntity)
    }


    class ToolsViewModelFactory( private val repository: AlarmRepository ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ToolsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ToolsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }


}
