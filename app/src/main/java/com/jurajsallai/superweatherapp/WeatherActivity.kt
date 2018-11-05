package com.jurajsallai.superweatherapp

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import com.jurajsallai.superweatherapp.databinding.ActivityWeatherBinding
import com.jurajsallai.superweatherapp.dialogfragment.AlertDialogFragment
import com.jurajsallai.superweatherapp.model.CurrentWeatherModel
import kotlinx.android.synthetic.main.activity_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt

class WeatherActivity : AppCompatActivity() {

    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        cityName = intent.getStringExtra("CITY")
        if (isOnline()) {
            loadData(cityName)
        }
    }



    private fun loadData(city: String) {
        val weatherAPI = WeatherService.create()
        val call = weatherAPI.getWeatherByCityName(city)
        call.enqueue(object : Callback<CurrentWeatherModel> {

            override fun onFailure(call: Call<CurrentWeatherModel>?, t: Throwable?) {
                onFailure()
            }
            override fun onResponse(call: Call<CurrentWeatherModel>?, response: Response<CurrentWeatherModel>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onFailure()
                }
            }
        })
    }

    private fun onFailure() {
        AlertDialogFragment().show(supportFragmentManager, getString(R.string.error_dialog_tag))
    }


    private fun onSuccess(currentWeatherModel: CurrentWeatherModel?) {
        val tempCel = round(currentWeatherModel?.main?.temp?.minus(273.15)!!)
        currentWeatherModel.main?.temp = tempCel

        val timeNow = SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime())
        currentWeatherModel.time = timeNow

        val icon = currentWeatherModel.weather?.get(0)?.icon
        var iconResource: Int? = null
        when (icon) {
            "01d" -> iconResource = R.drawable.icon01d
            "01n" -> iconResource = R.drawable.icon01n
            "02d" -> iconResource = R.drawable.icon02d
            "02n" -> iconResource = R.drawable.icon02n
            "03d" -> iconResource = R.drawable.icon03d
            "03n" -> iconResource = R.drawable.icon03n
            "04d" -> iconResource = R.drawable.icon04d
            "04n" -> iconResource = R.drawable.icon04n
            "09d" -> iconResource = R.drawable.icon09d
            "09n" -> iconResource = R.drawable.icon09n
            "10d" -> iconResource = R.drawable.icon10d
            "10n" -> iconResource = R.drawable.icon10n
        }
        currentWeatherModel.iconResource = iconResource

        val binding: ActivityWeatherBinding = DataBindingUtil.setContentView(this@WeatherActivity, R.layout.activity_weather)
        binding.weather = currentWeatherModel

        runOnUiThread {
            val drawable = ResourcesCompat.getDrawable(resources, iconResource!!, null);
            weatherImage.setImageDrawable(drawable)
        }
    }

    private fun isOnline(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        val networkStatus: Boolean = networkInfo?.isConnected ?: false

        if (networkStatus) {
            return networkStatus
        } else {
            AlertDialogFragment().show(supportFragmentManager, getString(R.string.error_dialog_tag))
        }
        return networkStatus
    }
}
