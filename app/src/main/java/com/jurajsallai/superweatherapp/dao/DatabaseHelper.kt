package com.jurajsallai.superweatherapp.dao

import android.content.ContentValues
import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jurajsallai.superweatherapp.datamodel.City

class DatabaseHelper(context: Context, name: String?,
                     factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "productDB.db"
        val TABLE_CITIES = "cities"
        val COLUMN_ID = "_id"
        val COLUMN_CITY = "cityName"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CITIES_TABLE = ("CREATE TABLE " +
                TABLE_CITIES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_CITY + ")")
        db.execSQL(CREATE_CITIES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITIES)
        onCreate(db)
    }

    fun addCity(city: City) {

        val values = ContentValues()
        values.put(COLUMN_CITY, city.cityName)

        val db = this.writableDatabase

        db.insert(TABLE_CITIES, null, values)

        db.close()
    }

    fun getAllCities(): MutableList<String> {
        val cityList = mutableListOf<String>()
        val query = "SELECT * FROM $TABLE_CITIES"

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val city = City(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1)
                )
                cityList.add(city.cityName!!)
            } while (cursor.moveToNext())
        }
        return cityList
    }


    fun deleteCity(city: City) {
        val db = this.writableDatabase
        db.delete(TABLE_CITIES, "$COLUMN_ID = ?", arrayOf(city.id.toString()))
        db.close();
    }

}

