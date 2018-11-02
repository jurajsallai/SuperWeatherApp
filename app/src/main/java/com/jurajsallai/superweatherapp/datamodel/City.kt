package com.jurajsallai.superweatherapp.datamodel

class City {
    var id: Int = 0
    var cityName: String? = null

    constructor(id: Int, cityName: String) {
        this.id = id
        this.cityName = cityName
    }

    constructor(cityName: String) {
        this.cityName = cityName
    }
}