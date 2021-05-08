package com.codinghub.goodhabitbeta.update

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bot.box.horology.annotation.DURATION
import bot.box.horology.annotation.SUNSIGN
import bot.box.horology.api.Horoscope
import bot.box.horology.delegate.Response
import bot.box.horology.hanshake.HorologyController
import bot.box.horology.pojo.Zodiac
import com.codinghub.goodhabitbeta.R
import com.codinghub.goodhabitbeta.databinding.ActivityHoroscopeBinding
import soup.neumorphism.NeumorphCardView
import java.text.SimpleDateFormat
import java.util.*

class HoroscopeActivity : AppCompatActivity() {
    lateinit var gridView: GridView
    private lateinit var presentCardView: NeumorphCardView
    private var isSecondTime =false
    private lateinit var ctx :Context
    private var horoscopeNames = arrayOf(
        "Aquarius",
        "Aries",
        "Cancer",
        "Capricorn",
        "Gemini",
        "Leo",
        "Libra",
        "Pisces",
        "Sagittarius",
        "Scorpio",
        "Taurus",
        "Virgo"
    )
    private var horoscopeImages = intArrayOf(
        R.drawable.h_aquarius,
        R.drawable.h_aries,
        R.drawable.h_cancer,
        R.drawable.h_capricorn,
        R.drawable.h_gemini,
        R.drawable.h_leo,
        R.drawable.h_libra,
        R.drawable.h_pisces,
        R.drawable.h_sagittarius,
        R.drawable.h_scorpio,
        R.drawable.h_taurus,
        R.drawable.h_virgo
    )
    lateinit var binding: ActivityHoroscopeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoroscopeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ctx= this@HoroscopeActivity

        val horoscopeAdapter = HoroscopeAdapter(this, horoscopeNames, horoscopeImages)
        binding.textViewDateHoroscope.text =
            SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date())
        binding.gridViewHoroscope.adapter = horoscopeAdapter
        binding.gridViewHoroscope.choiceMode= GridView.CHOICE_MODE_SINGLE
        binding.gridViewHoroscope.setItemChecked(1,true)
        val gridView = findViewById<GridView>(R.id.gridViewHoroscope)
        gridView.setSelection(0)
        gridView.setItemChecked(0,true)
        binding.gridViewHoroscope.setItemChecked(1,true)
        binding.gridViewHoroscope.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, position, _ ->
                run {
                    Toast.makeText(
                        this, "You CLicked " + horoscopeNames[+position],
                        Toast.LENGTH_SHORT
                    ).show()
                    val currCardView =
                        view.findViewById<NeumorphCardView>(R.id.cardViewItemHoroscope)
                    currCardView.setShapeType(1)
                    if (isSecondTime)
                        presentCardView.setShapeType(0)
                    isSecondTime = true
                    presentCardView = currCardView
                    val horoscope = getHHoroscope(position)
                    val cAries = HorologyController(object : Response {
                        override fun onResponseObtained(zodiac: Zodiac) {
                            println("here is the aries 02")
                            binding.textViewDataHoroscope.text = zodiac.horoscope
                            Log.d(
                                "debug",
                                "Your horoscope is " + zodiac.horoscope + "of sign = " + zodiac.sunSign
                            )
                        }

                        override fun onErrorObtained(errormsg: String) {
                            binding.textViewDataHoroscope.text = "Check your connection"
                            Log.d("debug", "Your horoscope is $errormsg")
                        }
                    })
                    cAries.requestConstellations(horoscope)
                }
            }

    }

    private fun getHHoroscope(position: Int):Horoscope{
        val horoscope:Horoscope
        when(position){
            0 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true).showLoader(true)
                    .requestSunSign(SUNSIGN.AQUARIUS).requestDuration(DURATION.TODAY).isDebuggable(
                        true
                    ).fetchHoroscope()
            }
            1 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true)
                    .requestSunSign(SUNSIGN.ARIES).requestDuration(DURATION.TODAY)
                    .isDebuggable(true).fetchHoroscope()
            }
            2 -> {
                horoscope = Horoscope.Zodiac(this).requestSunSign(SUNSIGN.CANCER).requestDuration(DURATION.TODAY).isDebuggable(true).fetchHoroscope()
            }
            3 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true).showLoader(true)
                    .requestSunSign(SUNSIGN.CAPRICORN).requestDuration(DURATION.TODAY).isDebuggable(
                        true
                    ).fetchHoroscope()
            }
            4 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true).showLoader(true)
                    .requestSunSign(SUNSIGN.GEMINI).requestDuration(DURATION.TODAY).isDebuggable(
                        true
                    ).fetchHoroscope()
            }
            5 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true).showLoader(true)
                    .requestSunSign(SUNSIGN.LEO).requestDuration(DURATION.TODAY)
                    .isDebuggable(true).fetchHoroscope()
            }
            6 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true).showLoader(true)
                    .requestSunSign(SUNSIGN.LIBRA).requestDuration(DURATION.TODAY)
                    .isDebuggable(true).fetchHoroscope()
            }
            7 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true).showLoader(true)
                    .requestSunSign(SUNSIGN.PISCES).requestDuration(DURATION.TODAY).isDebuggable(
                        true
                    ).fetchHoroscope()
            }
            8 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true).showLoader(true)
                    .requestSunSign(SUNSIGN.SAGITTARIUS).requestDuration(DURATION.TODAY)
                    .isDebuggable(
                        true
                    ).fetchHoroscope()
            }
            9 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true).showLoader(true)
                    .requestSunSign(SUNSIGN.SCORPIO).requestDuration(DURATION.TODAY).isDebuggable(
                        true
                    ).fetchHoroscope()
            }
            10 -> {
                horoscope = Horoscope.Zodiac(this)
                    .showLoader(true).showLoader(true)
                    .requestSunSign(SUNSIGN.TAURUS).requestDuration(DURATION.TODAY).isDebuggable(
                        true
                    ).fetchHoroscope()
            }
            else -> {
                horoscope = Horoscope.Zodiac(this).requestSunSign(SUNSIGN.VIRGO).requestDuration(DURATION.TODAY).isDebuggable(true).fetchHoroscope()

            }
        }
        return horoscope
    }

}
