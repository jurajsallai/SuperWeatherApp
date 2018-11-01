package com.jurajsallai.superweatherapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import kotlinx.android.synthetic.main.activity_weather.*


class MainActivity : AppCompatActivity() {

    lateinit var city: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO custom button style + vyriesit klikanie

        /***TESTING - SOMETHING LIKE SQL***/
        var listOfButtons: MutableList<View> = mutableListOf()
        val citiesFromSql = arrayOf("Levice", "London", "Nitra")
        var id = 0
        for (city in citiesFromSql) {
            val buttonX = Button(this)
            buttonX.id = id
            buttonX.text = city
    //        cityLayout.addView(buttonX)
            lv.addView(buttonX)
            // adding buttons (views) to List
            listOfButtons.add(id, buttonX)
            //lv.addFooterView(buttonX)
            id.inc()
        }

        lv.setOnItemClickListener { parent, view, position, id ->
            titleTextView.text = view.id.toString()
        }

//        listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
//            val o = prestListView.getItemAtPosition(position)
//            val str = o as prestationEco //As you are using Default String Adapter
//            Toast.makeText(baseContext, str.getTitle(), Toast.LENGTH_SHORT).show()
//        })


        /******/

        saveButton.setOnClickListener {

            city = enterCityEditText.text.toString()
            val buttonX = Button(this)
            buttonX.text = city
       //     cityLayout.addView(buttonX)
            // TODO nacitanie miest z databazy,po SAVE ulozenie a nacitanie znova
        }

//        button1.setOnClickListener {
//            city = enterCityEditText.text.toString()
//            checkWeatherByCity(city)
//        }

    }

    private fun checkWeatherByCity(city: String) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra("CITY", city)
        startActivity(intent)
    }


}
