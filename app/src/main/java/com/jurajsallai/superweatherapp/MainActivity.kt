package com.jurajsallai.superweatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jurajsallai.superweatherapp.DataModel.CurrentWeatherModel
import com.jurajsallai.superweatherapp.DialogFragment.AlertDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val city = tempEditText
        tempButton.setOnClickListener {
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
        tempTextView.text = tempCel
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
