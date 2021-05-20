package com.codinghub.goodhabitbeta.tools.alarm

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.codinghub.goodhabitbeta.R

class ListAdapterMissionAlarm(
    private val context: Context,
    private val title: Array<String>,
    private val description: Array<String>,
    private val icon: Array<Int>,
    private val selected: Array<Boolean>
) : BaseAdapter() {
    override fun getCount(): Int {
        return title.size
    }

    override fun getItem(position: Int): Any {
        return title[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view :View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_listview_mission_sheet, parent, false)
        val titleTextView = view.findViewById<TextView>(R.id.titleItemMissionSheet)
        titleTextView?.text = title[position]
        val descriptionTextView = view.findViewById<TextView>(R.id.descriptionItemMissionSheet)
        descriptionTextView?.text = description[position]
        val selectedImageView = view.findViewById<ImageView>(R.id.selectedItemMissionSheet)

        val iconImageView = view.findViewById<ImageView>(R.id.iconItemMissionSheet)
        iconImageView?.setImageResource(icon[position])
        if (position==0){
            descriptionTextView.visibility = View.GONE
            val param = titleTextView.layoutParams as ViewGroup.MarginLayoutParams
            param.bottomMargin = 0
            titleTextView.layoutParams = param
        }
        Log.d("mission","Called true/false")

        if (selected[position]){
            Log.d("mission","Called true")
            selectedImageView.visibility = View.VISIBLE
        }else{
            selectedImageView.visibility = View.INVISIBLE
            Log.d("mission","Called false")

        }

        return view

    }


}