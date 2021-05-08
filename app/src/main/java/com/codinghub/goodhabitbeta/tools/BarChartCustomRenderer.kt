package com.codinghub.goodhabitbeta.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler


class BarChartCustomRenderer(
    chart: BarDataProvider?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?,
    imageList: ArrayList<Bitmap?>,
    context: Context?
) : BarChartRenderer(chart, animator, viewPortHandler) {
    private var context: Context? = null
    private var imageList: ArrayList<Bitmap?>? = null

    init {
        this.context = context
        this.imageList = imageList
    }


    override fun drawValues(c: Canvas?) {
        val dataSets = mChart.barData.dataSets
        val valueOffsetPlus: Float = Utils.convertDpToPixel(22f)
        var negOffset: Float

        for (i in 0 until mChart.barData.dataSetCount) {
            val dataSet = dataSets[i]
            applyValueTextStyle(dataSet)
            val valueTextHeight: Int = Utils.calcTextHeight(mValuePaint, "8")
            negOffset = valueTextHeight + valueOffsetPlus
            val buffer = mBarBuffers[i]
            var left: Float
            var right: Float
            var top: Float
            var bottom: Float
            var j = 0
            while (j < buffer.buffer.size * mAnimator.phaseX) {
                left = buffer.buffer[j]
                right = buffer.buffer[j + 2]
                top = buffer.buffer[j + 1]
                bottom = buffer.buffer[j + 3]
                val x = (left + right) / 2f
                if (!mViewPortHandler.isInBoundsRight(x)) break
                if (!mViewPortHandler.isInBoundsY(top) || !mViewPortHandler.isInBoundsLeft(x)) {
                    j += 4
                    continue
                }
                val entry = dataSet.getEntryForIndex(j / 4)
                val variable = entry.y
                mValuePaint.textAlign = Paint.Align.CENTER
                if (variable > 0) {
//                    drawValue(
//                        c, dataSet.valueFormatter.toString(), variable, entry, i, x,
//                        (bottom + negOffset),
//                        dataSet.getValueTextColor(j / 4)
//                    )
                }
                val bitmap: Bitmap? = imageList?.get(j / 4)
                val scaledBitmap = bitmap?.let { getScaledBitmap(it) }
                c!!.drawBitmap(
                    scaledBitmap!!,
                    x - scaledBitmap.width / 2f,
                    bottom + 0.5f * negOffset - scaledBitmap.width / 2f,
                    null
                )
                j += 4
            }
        }

        super.drawValues(c)
    }

    private fun getScaledBitmap(bitmap: Bitmap): Bitmap? {
        val width = 18
        val height = 18
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}