package com.codinghub.goodhabitbeta.update
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.codinghub.goodhabitbeta.MySingleton
import com.codinghub.goodhabitbeta.R
import com.codinghub.goodhabitbeta.databinding.UpdateFragmentBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.update_fragment.*


class updateFragment : Fragment() {

    private var gps_enabled=false
    private var network_enabled =false
    private lateinit var lm : LocationManager
    private var net_loc :Location?=null
    private var gps_loc :Location?=null
    private var final_loc :Location?=null
    private var MY_PERMISSIONS_REQUEST_LOCATION =101
    private var provider : String ? = null

    private val sdf = SimpleDateFormat("h:mm a",Locale.getDefault())



    companion object {
        fun newInstance() = updateFragment()
    }

    private lateinit var viewModel: UpdateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        return inflater.inflate(R.layout.update_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateViewModel::class.java)
        horoscopeButtonUpdate.setOnClickListener {
            val intent = Intent(context, HoroscopeActivity::class.java)
            startActivity(intent)
        }
        lm = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        provider = lm.getBestProvider(Criteria(),false)
        gps_enabled= lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        network_enabled= lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        if (context?.let {
//                ActivityCompat.checkSelfPermission(
//                    it,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            } != PackageManager.PERMISSION_GRANTED && context?.let {
//                ActivityCompat.checkSelfPermission(
//                    it,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            } != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(context as Activity,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),MY_PERMISSIONS_REQUEST_LOCATION)
//        }
        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
        // TODO: Use the ViewModel
        if (network_enabled){
            net_loc =lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        if (gps_loc!=null&& net_loc!=null){
            final_loc = if (gps_loc!!.getAccuracy() > net_loc!!.getAccuracy())
                net_loc
            else
                gps_loc
        }else{
            if (gps_loc != null) {
                final_loc = gps_loc
            } else if (net_loc != null) {
                final_loc = net_loc
            }
        }
        val location_lati =final_loc?.latitude
        val location_long =final_loc?.longitude
        val apiKey ="8ff49986433c4ed16b54da8b94d6775a"
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=23.8608498&lon=86.4053626&appid=8ff49986433c4ed16b54da8b94d6775a"
        val url_new = "https://api.openweathermap.org/data/2.5/weather?lat=$location_lati&lon=$location_long&appid=$apiKey"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
//            textView.text = "Response: %s".format(response.toString())
                Log.d("debug_fragment", "response got successfully ")
                val weather = response.getJSONArray("weather").getJSONObject(0)
                val main = response.getJSONObject("main")
                val sys = response.getJSONObject("sys")

                textViewHumidity.text = main.getString("humidity").plus("%")
                val presentTemp = main.getString("temp")
                textViewTemp.text = (presentTemp.toDouble().toInt() - 273).toString().plus("\u00B0")
                textViewSunrise.text = getDateString(sys.getString("sunrise").toLong())
                textViewSunset.text = getDateString(sys.getString("sunset").toLong())
                textViewWeatherLocation.text = response.getString("name")
                val weathericon = weather.getString("icon")
                Log.d(
                    "debug",
                    "Location is " + response.getString("name") + "with lati=" + location_lati + " and log = " + location_long
                )
                //                    Log.e("Tag","Formatted Date"+getDateString(sys.getString("sunrise").toLong()))
                val image_url = "https://openweathermap.org/img/wn/$weathericon@2x.png"
                Glide.with(requireContext()).load(image_url).into(imageViewWeather)
                if (weathericon.last() == 'n') {
                    textViewWeatherDescription.text = weather.getString("main").plus(" night")
                } else {
                    textViewWeatherDescription.text = weather.getString("main").plus(" day")
                }

            },
            { error ->
                Log.d("debug_fragment", "response got unsuccessfully " + error.message)
// TODO: Handle error
            }
        )

// Access the RequestQueue through your singleton class.
        context?.let { MySingleton.getInstance(it).addToRequestQueue(jsonObjectRequest) }

    }
    private fun getDateString(time: Long) : String = sdf.format(time * 1000L)

}


private fun LocationManager.requestLocationUpdates(it: String, i1: Int, context: Context) {

}
