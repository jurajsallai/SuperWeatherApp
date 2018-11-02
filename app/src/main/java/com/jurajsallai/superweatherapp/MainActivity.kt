package com.jurajsallai.superweatherapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.AdapterView.OnItemClickListener
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.list_item_1.view.*


class MainActivity : AppCompatActivity() {

    lateinit var city: String
    lateinit var array: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO SimpleCursorAdapter for SQLLite
        array = arrayListOf("Brno", "Zlin")
        val adapter = ArrayAdapter<String>(this, R.layout.list_item_1,array)
        lv.adapter = adapter

        lv.setOnItemClickListener { parent, view, position, id ->
            city = view.text1.text.toString()
            checkWeatherByCity(city)
        }

        // TODO nacitanie miest z databazy,po SAVE ulozenie a nacitanie znova
        saveButton.setOnClickListener {
            city = enterCityEditText.text.toString()
            array.add(city)
            adapter.notifyDataSetChanged()
            lv.adapter = adapter
        }

    }

    private fun checkWeatherByCity(city: String) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra("CITY", city)
        startActivity(intent)
    }


}
