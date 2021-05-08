package com.codinghub.goodhabitbeta.tools

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.codinghub.goodhabitbeta.Constants
import com.codinghub.goodhabitbeta.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jem.rubberpicker.RubberRangePicker
import kotlinx.android.synthetic.main.cup_size_bottom_sheet_model.*
import kotlinx.android.synthetic.main.reminder_bottom_sheet.*
import java.util.*
import kotlin.properties.Delegates

class CupSizeBottomSheetDialogFragment(val sheetType: Int = 0) : BottomSheetDialogFragment() {
    private var selectedCup: Int = 0
    private lateinit var previousCup: ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (sheetType == 0) {
            inflater.inflate(R.layout.cup_size_bottom_sheet_model, container, false)
        } else {
            inflater.inflate(R.layout.reminder_bottom_sheet, container, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (sheetType == 0) {
            cupSize_100ml.setOnClickListener {
                updateColor(1, cupSize_100ml)
            }
            cupSize_200ml.setOnClickListener {
                updateColor(2, cupSize_200ml)
            }
            cupSize_300ml.setOnClickListener {
                updateColor(3, cupSize_300ml)
            }
            cupSize_500ml.setOnClickListener {
                updateColor(4, cupSize_500ml)
            }
            cupSize_1000ml.setOnClickListener {
                updateColor(5, cupSize_1000ml)
            }


            setCupOkButton.setOnClickListener {
                Log.d("debugSheet", "selected cup$selectedCup")
                Constants().cupSize = selectedCup
                dismiss()
            }
        }else{
            gapDefaultReminderSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val interval:Int = if (progress==0||progress==1){
                        1
                    }else{
                        progress/2
                    }
                    val stringInterval = "Every $interval hour "
                    gapDefaultTextViewWaterReminder.text =stringInterval
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })
            goalDefaultReminderSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    goalTextViewWaterReminder.text = progress.toString().plus(" ml")
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })
            durationDefaultReminderSeekBar.setOnRubberRangePickerChangeListener(object :
                RubberRangePicker.OnRubberRangePickerChangeListener{
                override fun onProgressChanged(
                    rangePicker: RubberRangePicker,
                    startValue: Int,
                    endValue: Int,
                    fromUser: Boolean
                ) {
                    defaultReminderStartTextView.text= startValue.toString().plus(" am")
                    defaultReminderEndTextView.text = endValue.toString().plus(" am")
                }

                override fun onStartTrackingTouch(
                    rangePicker: RubberRangePicker,
                    isStartThumb: Boolean
                ) {
                }

                override fun onStopTrackingTouch(
                    rangePicker: RubberRangePicker,
                    isStartThumb: Boolean
                ) {
                }

            })
        }

    }

    fun updateColor(cupNum: Int, imageView: ImageView) {
        ImageViewCompat.setImageTintList(
            imageView, ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.purpleTheme)
            )
        )
        if (selectedCup > 0) {
            previousCup.drawable.setTintList(null)
        }
        previousCup = imageView
        selectedCup = cupNum

    }

    public fun getCupSize(): Int{
        return selectedCup
    }

}
