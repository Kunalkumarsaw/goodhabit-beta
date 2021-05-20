package com.codinghub.goodhabitbeta.tools.alarm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.codinghub.goodhabitbeta.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.label_sheet_alarm.*
import kotlinx.android.synthetic.main.mission_sheet_alarm.*
import kotlinx.android.synthetic.main.wakeup_check_sheet.*
import soup.neumorphism.NeumorphButton

class AlarmBottomSheetFragment(
    private val sheetNumber: Int,
    private val viewModel: AlarmHomeViewModel
) : BottomSheetDialogFragment() {

    var labelText: String = ""
//
//    private val viewModel: AlarmHomeViewModel by viewModels {
//        AlarmHomeViewModel.AlarmViewModelFactory(
//            (requireActivity().application as AlarmApplication).repository
//        )
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return when (sheetNumber) {
            0 -> {
                inflater.inflate(R.layout.mission_sheet_alarm, container, false)
            }
            9->{
                inflater.inflate(R.layout.wakeup_check_sheet, container, false)
            }
            10 -> {
                inflater.inflate(R.layout.label_sheet_alarm, container, false)
            }
            else -> {
                inflater.inflate(R.layout.label_sheet_alarm, container, false)
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        when (sheetNumber) {

            0 -> {
                val myListAdapter = ListAdapterMissionAlarm(
                    requireContext(),
                    viewModel.missionTitle,
                    viewModel.missionDescription,
                    viewModel.missionIcons,
                    viewModel.selectedMission
                )

                listViewMissionSheet.adapter = myListAdapter

                backButtonMissionSheet.visibility = View.GONE

                listViewMissionSheet.setOnItemClickListener { parent, view, position, id ->
                    Toast.makeText(
                        requireContext(),
                        "You clicked ${viewModel.missionTitle[position]} !",
                        Toast.LENGTH_LONG
                    ).show()

                    backButtonMissionSheet.visibility = View.VISIBLE
                    listViewMissionSheet.visibility = View.GONE

                    // common point of layout inflate for Memory ,Math and Shake Missions

                    val childViewMemory =
                        View.inflate(requireContext(), R.layout.memory_mission_sheet, null)
                    val numberPickerMemory =
                        childViewMemory.findViewById<NumberPicker>(R.id.numberPickerMemoryMission)
                    val saveMemoryMission =
                        childViewMemory.findViewById<NeumorphButton>(R.id.saveMemoryMission)
                    val titleMemoryMission =
                        childViewMemory.findViewById<TextView>(R.id.titleMemoryMission)
                    val difficultyLevelTitleMemoryMission =
                        childViewMemory.findViewById<TextView>(R.id.difficultyLevelTitleMemoryMission)
                    val seekBarDifficultyLevelMemoryMission =
                        childViewMemory.findViewById<SeekBar>(R.id.difficultyLevelMemoryMission)
                    val iconMemoryMission =
                        childViewMemory.findViewById<ImageView>(R.id.iconMemoryMission)
                    val iconTitleMemoryMission =
                        childViewMemory.findViewById<TextView>(R.id.iconTitleMemoryMission)

                    // selection of one by one missions

                    when (position) {
                        0 -> {
                            listViewMissionSheet.visibility = View.VISIBLE
                            updateSelectedMission(position)
                            viewModel.alarmMission = position
                            backButtonMissionSheet.visibility = View.GONE
                            myListAdapter.notifyDataSetChanged()
                        }
                        1 -> {
                            val childView =
                                View.inflate(requireContext(), R.layout.typing_mission_sheet, null)
                            val numberPicker =
                                childView.findViewById<NumberPicker>(R.id.numberPickerTypingMission)
                            val generalRadioButton =
                                childView.findViewById<RadioButton>(R.id.radioGeneralTypingMission)
                            val motivationalRadioButton =
                                childView.findViewById<RadioButton>(R.id.radioMotivationalTypingMission)
                            val saveTypeMission =
                                childView.findViewById<NeumorphButton>(R.id.saveTypingMission)
                            numberPicker.minValue = 1
                            numberPicker.maxValue = 15
                            numberPicker.value = 3
                            motivationalRadioButton.isChecked = true
                            generalRadioButton.isChecked = false
                            saveTypeMission.setOnClickListener {
                                val newStatus = viewModel.getFeatureStatus.value
                                newStatus!![0] = "Typing"
                                viewModel.updateFeatureStatus(newStatus)
                                viewModel.alarmMission = position
                                updateSelectedMission(position)
                                dismiss()
                            }

                            linerLayoutMissionSheet.addView(childView)
                        }
                        2 -> {
                            titleMemoryMission.text = getString(R.string.title_memory_mission)
                            iconMemoryMission.setImageResource(R.drawable.memory_icon)
                            iconTitleMemoryMission.text = getString(R.string.memory)
                            numberPickerMemory.minValue = 1
                            numberPickerMemory.maxValue = 50
                            numberPickerMemory.value = 3
                            saveMemoryMission.setOnClickListener {
                                val newStatus = viewModel.getFeatureStatus.value
                                newStatus!![0] = "Memory"
                                viewModel.updateFeatureStatus(newStatus)
                                viewModel.alarmMission = position
                                updateSelectedMission(position)
                                dismiss()
                            }
                            seekBarDifficultyLevelMemoryMission.max = 4
                            seekBarDifficultyLevelMemoryMission.progress = 2
                            difficultyLevelTitleMemoryMission.text = setMemoryProgress(2, position)
                            linerLayoutMissionSheet.addView(childViewMemory)
                        }
                        3 -> {
                            titleMemoryMission.text = getString(R.string.title_shake_mission)
                            iconMemoryMission.setImageResource(R.drawable.shake_icon)
                            iconTitleMemoryMission.text = getString(R.string.shake)
                            numberPickerMemory.minValue = 5
                            numberPickerMemory.maxValue = 100
                            numberPickerMemory.value = 30
                            saveMemoryMission.setOnClickListener {
                                val newStatus = viewModel.getFeatureStatus.value
                                newStatus!![0] = getString(R.string.shake)
                                viewModel.updateFeatureStatus(newStatus)
                                viewModel.alarmMission = position
                                updateSelectedMission(position)
                                dismiss()
                            }
                            seekBarDifficultyLevelMemoryMission.max = 2
                            seekBarDifficultyLevelMemoryMission.progress = 1
                            difficultyLevelTitleMemoryMission.text = setMemoryProgress(1, position)
                            linerLayoutMissionSheet.addView(childViewMemory)

                        }
                        4 -> {
                            titleMemoryMission.text = getString(R.string.title_math_mission)
                            iconMemoryMission.setImageResource(R.drawable.math_icon)
                            iconTitleMemoryMission.text = getString(R.string.math)
                            numberPickerMemory.minValue = 1
                            numberPickerMemory.maxValue = 50
                            numberPickerMemory.value = 3
                            saveMemoryMission.setOnClickListener {
                                val newStatus = viewModel.getFeatureStatus.value
                                newStatus!![0] = getString(R.string.math)
                                viewModel.updateFeatureStatus(newStatus)
                                viewModel.alarmMission = position
                                updateSelectedMission(position)
                                dismiss()
                            }
                            seekBarDifficultyLevelMemoryMission.max = 5
                            seekBarDifficultyLevelMemoryMission.progress = 3
                            difficultyLevelTitleMemoryMission.text = setMemoryProgress(3, position)
                            linerLayoutMissionSheet.addView(childViewMemory)

                        }
                        5 -> {
                            val childView =
                                View.inflate(requireContext(), R.layout.barcode_mission_sheet, null)
                            val saveTypeMission =
                                childView.findViewById<NeumorphButton>(R.id.saveBarcodeMission)
                            saveTypeMission.setOnClickListener {
                                val newStatus = viewModel.getFeatureStatus.value
                                newStatus!![0] = "QR/Barcode"
                                viewModel.updateFeatureStatus(newStatus)
                                viewModel.alarmMission = position
                                updateSelectedMission(position)
                                dismiss()
                            }

                            linerLayoutMissionSheet.addView(childView)
                        }
                    }

                    seekBarDifficultyLevelMemoryMission.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            val textSeekBar =
                                setMemoryProgress(progress = progress, position = position)
                            difficultyLevelTitleMemoryMission.text = textSeekBar
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        }


                    })

                    backButtonMissionSheet.setOnClickListener {
                        linerLayoutMissionSheet.removeAllViews()
                        listViewMissionSheet.visibility = View.VISIBLE
                        backButtonMissionSheet.visibility = View.GONE
                    }

                }
            }

            9->{
                    saveWakeupCheck.setOnClickListener {
                        val newStatus = viewModel.getFeatureStatus.value
                        val wakeupStatus = when(radioGroupWakeupCheck.checkedRadioButtonId){
                            radioNoneWakeup.id->{
                               radioNoneWakeup.text.toString()
                            }
                            radioOneMinWakeup.id->{
                               radioOneMinWakeup.text.toString()
                            }
                            radioThreeMinWakeup.id->{
                                 radioThreeMinWakeup.text.toString()
                            }
                            radioFiveMinWakeup.id->{
                               radioFiveMinWakeup.text.toString()
                            }
                            radioSevenMinWakeup.id->{
                                radioSevenMinWakeup.text.toString()
                            }
                            radioTenMinWakeup.id->{
                                radioTenMinWakeup.text.toString()
                            }
                            else -> {
                                "Off"
                            }
                        }
                        newStatus!![2] = wakeupStatus
                        viewModel.updateFeatureStatus(newStatus)
                        dismiss()
                    }
            }

            10 -> {

                saveLabelSheet.setOnClickListener {
                    if (textLabelSheet.text.toString().trim().isNotEmpty()) {
                        val newStatus = viewModel.getFeatureStatus.value
                        newStatus!![3] = textLabelSheet.text.toString().trim()
                        viewModel.updateFeatureStatus(newStatus)
                    }
                    Log.d(
                        "label",
                        "View model called successfully with "
                    )
                    // Log.d("label"," View Model Label is ${viewModel.getFeatureStatus.value!![3]}")
                    dismiss()
                }
            }
        }
    }

    private fun setMemoryProgress(progress: Int, position: Int): String {
        var text = ""

        when (position) {
            2 -> {
                text = when (progress) {
                    0 -> {
                        "Very easy(3X3) tiles"
                    }
                    1 -> {
                        "Easy(3X3) tiles"
                    }
                    2 -> {
                        "Easy(4X4) tiles"
                    }
                    3 -> {
                        "Normal(5X5) tiles"
                    }
                    4 -> {
                        "Hard(6X6) tiles"
                    }
                    else -> {
                        "Very easy(3X3) tiles"
                    }
                }
            }
            3 -> {
                text = when (progress) {
                    0 -> {
                        "Easy shake"
                    }
                    1 -> {
                        "Normal shake"
                    }
                    2 -> {
                        "Hard shake"
                    }
                    else -> {
                        "Normal shake"
                    }
                }
            }
            4 -> {
                text = when (progress) {
                    0 -> {
                        "3+8=?"
                    }
                    1 -> {
                        "23+17=?"
                    }
                    2 -> {
                        "43+24+34=?"
                    }
                    3 -> {
                        "(72x6)+32=?"
                    }
                    4 -> {
                        "(37x11)+321=?"
                    }
                    5->{
                        "(162x87)+1878=?"
                    }
                    else -> {
                        "Very easy(3X3) tiles"
                    }
                }

            }
        }
        return text
    }

    private fun updateSelectedMission(position: Int) {
        viewModel.selectedMission.forEachIndexed { index, _ ->
            viewModel.selectedMission[index] = index == position
        }
    }
}