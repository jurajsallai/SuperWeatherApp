package com.jurajsallai.superweatherapp

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jurajsallai.superweatherapp.databinding.ActivityMainBinding
import com.jurajsallai.superweatherapp.datamodel.CurrentWeatherModel
import com.jurajsallai.superweatherapp.dialogfragment.AlertDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val city = enterCityEditText
        saveButton.setOnClickListener {
            loadData(city.text.toString())
        }

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
                        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
                        binding.weather = response.body()
//                  TODO when have correct POJO with iconId
//                        runOnUiThread {
//                            drawable = resources.getDrawable(response.body().getIconId)
//                        }
                        onSuccess(response.body())
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
        val tempCel = (currentWeatherModel?.main?.temp?.minus(273.15)).toString() + "° C"

        savedCitiesTextView.text = tempCel
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
