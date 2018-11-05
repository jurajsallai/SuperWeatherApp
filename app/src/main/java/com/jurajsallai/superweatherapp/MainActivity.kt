package com.jurajsallai.superweatherapp

import android.content.Context
import android.content.Intent
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

import kotlinx.android.synthetic.main.activity_main.*
import com.jurajsallai.superweatherapp.dao.DatabaseHelper
import kotlinx.android.synthetic.main.list_item_1.view.*
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.jurajsallai.superweatherapp.databinding.ActivityMainBinding
import com.jurajsallai.superweatherapp.databinding.ActivityWeatherBinding
import com.jurajsallai.superweatherapp.dialogfragment.AlertDialogFragment
import com.jurajsallai.superweatherapp.model.CurrentWeatherModel
import kotlinx.android.synthetic.main.list_item_1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var cityName: String
    lateinit var cities: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DatabaseHelper(this, null, null, 1)
        cities = db.getAllCities()

        val adapter = ArrayAdapter<String>(this, R.layout.list_item_1, R.id.cityName, cities)
        listView.adapter = adapter

        if (isOnline()) {
            for (cityName in cities) {
                loadData(cityName)
            }
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            cityName = view.cityName.text.toString()
            checkWeatherByCity(cityName)
        }

        listView.isLongClickable

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val cityName = view.cityName.text.toString()
            cities.remove(cityName)
            db.deleteCity(cityName)
            adapter.notifyDataSetChanged()
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(100)
                }
            }
            return@setOnItemLongClickListener true
        }

        addNewCityButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkWeatherByCity(city: String) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra("CITY", city)
        startActivity(intent)
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
        val tempCel = (currentWeatherModel?.main?.temp?.minus(273.15))
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
