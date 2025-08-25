package com.example.weatherapp


data class Location(
    val name: String,
    val region: String,
    val country: String
)

data class Current(
    val temp_c: Double
)

data class WeatherResponse(
    val location: Location,
    val current: Current
)
