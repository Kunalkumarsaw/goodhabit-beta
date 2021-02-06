package com.codinghub.goodhabitbeta.update

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.codinghub.goodhabitbeta.R

class HoroscopeAdapter(
    private val context: Context,
    private val horoscopeNames: Array<String>,
    private val horoscopeImages: IntArray
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    override fun getCount(): Int {
        return horoscopeNames.size
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        return horoscopeNames[position]
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        return 0
        TODO("Not yet implemented")
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.item_view_grid_horoscope, null)
        }
        imageView = convertView!!.findViewById(R.id.imageVIew_itemHoroscope)
        textView = convertView.findViewById(R.id.textViewTitleItemHoroscope)
        imageView.setImageResource(horoscopeImages[position])
        textView.text = horoscopeNames[position]
        return convertView
    }

}
