package com.jurajsallai.superweatherapp

import com.jurajsallai.superweatherapp.model.CurrentWeatherModel
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather/")
    fun getWeatherByCityName(@Query("q") city: String,
                             @Query("appid") key: String = KEY): Call<CurrentWeatherModel>


    companion object Factory {

        const val KEY = "b0f85bdd4d4a7450e6f1c3c3d8a7382e";

        fun create(): WeatherService {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .build()

            return retrofit.create(WeatherService::class.java);
        }
    }
}