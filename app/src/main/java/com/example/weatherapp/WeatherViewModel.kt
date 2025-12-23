package com.example.weatherapp

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date

class WeatherViewModel : ViewModel() {

    private val _userinput = mutableStateOf("")
    val userInput: State<String> = _userinput

    private val _state = mutableStateOf(WeatherState())
    val state_for_comp: State<WeatherState> = _state

    val api = RetrofitIObj.apiInstance
    val api2 = RetrofitIObj2.apiInstance2
    val apiKey = BuildConfig.WEATHER_API_KEY


    fun onUserInputChange(input: String) {
        _userinput.value = input
    }

    fun getData() {
        viewModelScope.launch {
            try {
                val response = api.getWeather(apikey = apiKey, city = _userinput.value)
                val response2 = api2.getForecast(apikey = apiKey, city = _userinput.value)



                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {
                        _state.value = _state.value.copy(
                            temp = body.current.temp_c,
                            City = body.location.name,
                            Country = body.location.country,
                            State = body.location.region,
                            TimeZone = body.location.tz_id,
                            Humidity = body.current.humidity
                        )
                    } else {
                        println("Body is Null")
                    }
                } else {
                    println("Invalid response: ${response.errorBody()?.string()}")
                }
                if (response2.isSuccessful) {
                    val body2 = response2.body()
                    if (body2 != null) {

                        val now = Date()
                        val next8Hours = body2.forecast.forecastday[0].hour
                            .mapNotNull { hourData ->
                                val hourDate = hourData.time.toDate() // convert string to Date
                                if (hourDate?.after(now) == true) hourData else null
                            }
                            .take(8)

                        _state.value = _state.value.copy(
                            ChanceOfRain = body2.forecast.forecastday[0].day.daily_chance_of_rain,
                            HourlyForecast = next8Hours
                        )
                    } else {
                        println("Body is Null")
                    }
                } else {
                    println("Invalid response: ${response2.errorBody()?.string()}")
                }
                if (response.isSuccessful && response2.isSuccessful) {
                    _userinput.value = ""
                }
            } catch (e: Exception) {
                println("Error ${e.message} occurred!")
            }
        }
    }

    fun getWeatherByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val query = "$lat,$lon"
                Log.d("WeatherViewModel", "Fetching weather with query=$query")
                val response = api.getWeather(apikey = apiKey, city = query)
                val response2 = api2.getForecast(apikey = apiKey, city = query)

                if (response.isSuccessful && response2.isSuccessful) {
                    val body = response.body()
                    val body2 = response2.body()
                    if (body != null && body2 != null) {
                        val now = Date() // Get current time
                        val next8Hours = body2.forecast.forecastday[0].hour // Take today's hourly forecast
                            .mapNotNull { hourData ->
                                val hourDate = hourData.time.toDate() // convert string to Date
                                if (hourDate?.after(now) == true) hourData else null// takes each element of the list and checks if it is greater than the current hour(now). if yes, returns it or else returns null.
                            }
                            .take(8) // returns a new list with first n items of the og list

                        _state.value = _state.value.copy(
                            temp = body.current.temp_c,
                            City = body.location.name,
                            Country = body.location.country,
                            State = body.location.region,
                            TimeZone = body.location.tz_id,
                            Humidity = body.current.humidity,
                            ChanceOfRain = body2.forecast.forecastday[0].day.daily_chance_of_rain,
                            HourlyForecast = next8Hours
                        )
                    }
                } else {
                    println(
                        "Error fetching weather: ${
                            response.errorBody()?.string()
                        } / ${response2.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                println("Error fetching weather by coordinates: ${e.message}")
            }
        }
    }

}


data class WeatherState(
    val temp: Double = 0.0,
    val City: String = "",
    val State: String = "",
    val Country: String = "",
    val TimeZone: String = "Asia/Kolkata",
    val Humidity: Int = 0,
    val ChanceOfRain: Int = 0,
    val HourlyForecast: List<HourlyData>? = emptyList()
)