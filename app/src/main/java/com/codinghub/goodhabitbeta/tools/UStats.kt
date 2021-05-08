package com.codinghub.goodhabitbeta.tools

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*


class UStats {
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("M-d-yyyy HH:mm:ss",Locale.getDefault())


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun getUsageStatsList(context: Context?): MutableList<UsageStats>? {
        val usm: UsageStatsManager? = context?.let { getUsageStatsManager(it) }
        val calendar: Calendar = Calendar.getInstance()
        val endTime: Long = calendar.getTimeInMillis()
        calendar.add(Calendar.YEAR, -1)
        val startTime: Long = calendar.getTimeInMillis()
        Log.d(TAG, "Range start:" + dateFormat.format(startTime))
        Log.d(TAG, "Range end:" + dateFormat.format(endTime))

            return usm?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
    }


    private fun printUsageStats(usageStatsList: List<UsageStats>) {
        for (u in usageStatsList) {
            Log.d(
                TAG, "Pkg: " + u.packageName + "\t" + "ForegroundTime: "
                        + u.totalTimeInForeground
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun printCurrentUsageStatus(context: Context?) {
        printUsageStats(getUsageStatsList(context) as List<UsageStats>)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun getUsageStatsManager(context: Context): UsageStatsManager {
        return context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }
}
