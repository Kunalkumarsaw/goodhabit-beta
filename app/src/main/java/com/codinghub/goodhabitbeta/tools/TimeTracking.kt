package com.codinghub.goodhabitbeta.tools

import android.app.Activity
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codinghub.goodhabitbeta.R
import com.codinghub.goodhabitbeta.databinding.ActivityTimeTrackingBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.activity_time_tracking.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TimeTracking : AppCompatActivity() {
    val TAG = "TimeTracking"
    lateinit var binding: ActivityTimeTrackingBinding
    var granted = false
    val MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 101
    val sdf = SimpleDateFormat("yyyyy.MMMM.dd GGG hh:mm:ss aaa", Locale.getDefault())
    var xAxis = arrayListOf<String>(
        "WhatsApp",
        "Spotify",
        "Pinerest",
        "Youtube",
        "Facebook",
        "Snapchat",
        "Telegram",
        "Instagram"
    )
    var yAxis = arrayListOf<String>(
        "60",
        "20",
        "30",
        "80",
        "40",
        "25",
        "35",
        "55"
    )
    private var imageList: ArrayList<Bitmap?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "On Create Time Tracking called")
        binding = ActivityTimeTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButtonTimeTracking.setOnClickListener {
            onBackPressed()
        }
        val appOpsManager =
            applicationContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var mode = 0
        mode = if (android.os.Build.VERSION.SDK_INT >= 29) {
            appOpsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), this.packageName
            )
        } else {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), this.packageName
            )
        }

        val granted = mode == AppOpsManager.MODE_ALLOWED
        if (granted) {
            accessTimeForApps()

        } else {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivityForResult(intent, MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS)
        }
        binding.timeTrackingEyeButton.setOnClickListener {
            Log.d(TAG, "Clicked on the eye button")
            val usageStatsManager =
                getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager // Context.USAGE_STATS_SERVICE);

            val endCal: Calendar = Calendar.getInstance()
            endCal.set(Calendar.DAY_OF_MONTH, 7)
            endCal.set(Calendar.YEAR, 2021)
            val beginCal: Calendar = Calendar.getInstance()
            beginCal.set(Calendar.DAY_OF_MONTH, 7)
            beginCal.set(Calendar.YEAR, 2021)
            beginCal.set(Calendar.HOUR_OF_DAY, 0)
            beginCal.set(Calendar.MINUTE, 5)

            Log.d(
                TAG,
                "Begin time :- ${sdf.format(beginCal.time.time)} of mili Sec :- ${beginCal.time.time} AND End time :- ${
                    sdf.format(
                        endCal.time.time
                    )
                } of mili Sec :- ${endCal.time.time} "
            )


            val queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST,
                beginCal.time.time,
                endCal.time.time
            )
            for (app in queryUsageStats) {
                Log.d(TAG, app.packageName + " | " + (app.totalTimeInForeground / 60000).toFloat())
            }
        }
        val bitmap:Bitmap?= BitmapFactory.decodeResource(this.resources, R.drawable.bulb_icon)

        if (bitmap != null) {
            imageList?.add(bitmap)
            imageList?.add(bitmap)
            imageList?.add(bitmap)
            imageList?.add(bitmap)
            imageList?.add(bitmap)
            imageList?.add(bitmap)
            imageList?.add(bitmap)
            imageList?.add(bitmap)
        }


        val chart = binding.chart
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)

        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = 7
        xAxis.setDrawLabels(false)


        val leftAxis: YAxis = chart.axisLeft
        leftAxis.axisLineColor = Color.WHITE
        leftAxis.setDrawGridLines(false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        val rightAxis: YAxis = chart.axisRight
        rightAxis.isEnabled = false
        val l: Legend = chart.legend
        l.isEnabled = false
        setData()


        if (!granted) {

        }


    }

    private fun setData() {

        val yVals1 = ArrayList<BarEntry>()
        for (i in 0 until yAxis.size) {
            yVals1.add(BarEntry(i.toFloat(), yAxis.get(i).toFloat()))
        }

        val set1: BarDataSet = BarDataSet(yVals1, "")

        set1.setColors(Color.BLUE)
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)

        val data = BarData(dataSets)
        data.setDrawValues(false)
        chart.data = data
        chart.setScaleEnabled(false)
//        chart.renderer = BarChartCustomRenderer(
//            chart,
//            chart.animator,
//            chart.viewPortHandler,
//            imageList,
//            this
//        )
        chart.setExtraOffsets(0F, 0F, 0F, 20F)
    }

    private fun accessTimeForApps() {
        val endCal: Calendar = Calendar.getInstance()
        endCal.set(Calendar.DAY_OF_MONTH, 6)
        endCal.set(Calendar.YEAR, 2021)
        endCal.set(Calendar.HOUR_OF_DAY, 23)
        endCal.set(Calendar.MINUTE, 55)
        val beginCal: Calendar = Calendar.getInstance()
        beginCal.set(Calendar.DAY_OF_MONTH, 6)
        beginCal.set(Calendar.YEAR, 2021)
        beginCal.set(Calendar.HOUR_OF_DAY, 0)
        beginCal.set(Calendar.MINUTE, 5)
        val usageStatsManager =
            getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager // Context.USAGE_STATS_SERVICE);
        val lUsageStatsMap: Map<String, UsageStats> = usageStatsManager.queryAndAggregateUsageStats(
            beginCal.time.time,
            endCal.time.time
        )
        val totalTimeUsageInMillis =
            lUsageStatsMap["com.google.android.youtube"]!!.totalTimeInForeground
        val totalTimeUsageInMillis_W4b = lUsageStatsMap["com.whatsapp.w4b"]!!.totalTimeInForeground
        val totalTimeUsageInMillis_GoodHabit =
            lUsageStatsMap["com.codinghub.goodhabitbeta"]!!.totalTimeInForeground
        val totalTimeUsageInMillis_Watsapp = lUsageStatsMap["com.whatsapp"]!!.totalTimeInForeground

        Log.d(TAG, "TIme spend on Youtube is ${(totalTimeUsageInMillis / 60000).toFloat()}")
        Log.d(TAG, "TIme spend on What's B4B is ${(totalTimeUsageInMillis_W4b / 60000).toFloat()}")

        Log.d(
            TAG,
            "TIme spend on Good Habit is ${(totalTimeUsageInMillis_GoodHabit / 60000).toFloat()}"
        )
        Log.d(
            TAG,
            "TIme spend on What's app is ${(totalTimeUsageInMillis_Watsapp / 60000).toFloat()}"
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS -> accessTimeForApps()
                else -> {
                    // Result wasn't from Google Fit
                }
            }
            else -> {
                // Permission not granted
                Log.d("debugTracking", "Permission for fit not granted.")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}