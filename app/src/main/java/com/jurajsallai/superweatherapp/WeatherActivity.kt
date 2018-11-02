package com.jurajsallai.superweatherapp

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jurajsallai.superweatherapp.databinding.ActivityWeatherBinding
import com.jurajsallai.superweatherapp.datamodel.CurrentWeatherModel
import com.jurajsallai.superweatherapp.dialogfragment.AlertDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {

    private lateinit var city: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        city = intent.getStringExtra("CITY")
        loadData(city)
    }

    private fun loadData(city: String) {
        if (isOnline()) {
            val weatherAPI = WeatherService.Factory.create()
            val call = weatherAPI.getWeatherByCityName(city) as Call<CurrentWeatherModel>
            call.enqueue(object : Callback<CurrentWeatherModel> {

                override fun onFailure(call: Call<CurrentWeatherModel>?, t: Throwable?) {
                    onFailure()
                }

                override fun onResponse(call: Call<CurrentWeatherModel>?, response: Response<CurrentWeatherModel>) {
                    if (response.isSuccessful) {
                        onSuccess(response.body())
                        val binding: ActivityWeatherBinding = DataBindingUtil.setContentView(this@WeatherActivity, R.layout.activity_weather)
                        binding.weather = response.body()
//                  TODO when have correct POJO with iconId
//                        runOnUiThread {
//                            drawable = resources.getDrawable(response.body().getIconId)
//                        }
                    } else {
                        onFailure()
                    }
                }
            })
        }
    }

    private fun onFailure() {
        AlertDialogFragment().show(supportFragmentManager, getString(R.string.error_dialog_tag))
    }

    private fun onSuccess(currentWeatherModel: CurrentWeatherModel?) {
        val tempCel = (currentWeatherModel?.main?.temp?.minus(273.15)) //.toString() + "° C"
        currentWeatherModel?.main?.temp = tempCel
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
