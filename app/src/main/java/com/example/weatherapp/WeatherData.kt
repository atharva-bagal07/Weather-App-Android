package com.example.weatherapp


data class Location(
    val name: String,
    val region: String,
    val country: String,
    val tz_id: String,
    val localtime: String
)

data class Current(
    val temp_c: Double,
    val humidity: Int
)

data class WeatherResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast

)
data class Day(
    val daily_chance_of_rain: Int)

data class ConditionIcon(
    val icon: String
)
data class HourlyData(
    val time: String,
    val condition: ConditionIcon
)
data class Forecastday(
    val day: Day,
    val hour: List<HourlyData>)

data class Forecast(
    val forecastday: List<Forecastday>
)