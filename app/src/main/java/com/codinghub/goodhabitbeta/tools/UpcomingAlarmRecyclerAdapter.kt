package com.codinghub.goodhabitbeta.tools

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.codinghub.goodhabitbeta.R
import com.codinghub.goodhabitbeta.tools.alarm.AlarmEntity

class UpcomingAlarmRecyclerAdapter(private val alarms: List<AlarmEntity>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<UpcomingAlarmRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val radioButton: RadioButton = view.findViewById(R.id.radioButtonUpcoming)
        val timeUpcoming: TextView = view.findViewById(R.id.timeUpcoming)
        val labelUpcoming: TextView = view.findViewById(R.id.labelUpcoming)
        val recurringUpcoming: TextView = view.findViewById(R.id.recurringUpcoming)
        val spinner: Spinner = view.findViewById(R.id.spinnerUpcoming)
        val imageButton:ImageButton = view.findViewById(R.id.menuUpcoming)
        fun bind(alarm: AlarmEntity,clickListener: OnItemClickListener)
        {
            radioButton.setOnClickListener {
                alarm.isStarted = !alarm.isStarted
                clickListener.onRadioButtonClicked(alarm)
            }
            itemView.setOnClickListener {
                clickListener.onItemClicked(alarm)
            }

            imageButton.setOnClickListener {
                clickListener.onDropDownClicked(spinner,alarm)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_upcoming_alarm, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val alarm = alarms[position]

        holder.apply {
            radioButton.isChecked = alarm.isStarted
            val time: String = "" + alarm.hour + ":" + alarm.minute
            timeUpcoming.text = time
            labelUpcoming.text = alarm.alarmLabel
            if (alarm.getRecurringDays() == null) {
                recurringUpcoming.visibility = View.GONE
                val param = timeUpcoming.layoutParams as ViewGroup.MarginLayoutParams
                param.bottomMargin = 0
                timeUpcoming.layoutParams = param
            } else {
                recurringUpcoming.text = alarm.getRecurringDays()
            }
        }

        holder.bind(alarm,itemClickListener)


    }

    override fun getItemCount(): Int {
        return alarms.size
    }
}

interface OnItemClickListener{
    fun onItemClicked(alarm: AlarmEntity)
    fun onRadioButtonClicked(alarm: AlarmEntity)
    fun onDropDownClicked(spinner: Spinner,alarm: AlarmEntity)
}