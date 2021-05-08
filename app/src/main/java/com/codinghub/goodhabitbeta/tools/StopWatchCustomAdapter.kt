package com.codinghub.goodhabitbeta.tools

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.codinghub.goodhabitbeta.R
import kotlinx.android.synthetic.main.item_view_list_stopwatch.view.*

class StopWatchCustomAdapter(
    private val context: Context,
    private val dataSource: MutableList<String>
) : BaseAdapter() {
    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.item_view_list_stopwatch, parent, false)
        rowView.findViewById<TextView>(R.id.lapseTimeStopWatch).text = dataSource[position]
        rowView.lapseIndexStopWatch.text = "Lapse".plus(position+1)
        return rowView    }
}