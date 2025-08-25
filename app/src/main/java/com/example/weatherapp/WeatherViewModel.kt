package com.example.weatherapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.internal.GsonBuildConfig
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val _userinput = mutableStateOf("")
    val userInput: State<String> = _userinput

    private val _state = mutableStateOf(WeatherState())
    val state_for_comp: State<WeatherState> = _state

    val api = RetrofitIObj.apiInstance
    val apiKey = BuildConfig.Weather_api_key


    fun onUserInputChange(input: String){
        _userinput.value = input
    }

    fun getData(){
        viewModelScope.launch {
            try {
                val response = api.getWeather(apikey = apiKey, city = _userinput.value)
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        _state.value = _state.value.copy(
                            temp = body.current.temp_c,
                            City = body.location.name,
                            Country = body.location.country,
                            State = body.location.region
                        )
                    }
                    else{
                        println("Body is Null")
                    }
                }
                else {
                    println("Invalid response: ${response.errorBody()?.string()}")
                }
            }
            catch (e: Exception){
                println("Error ${e.message} occurred!")
            }
        }
    }
}


data class WeatherState(
    val temp: Double = 0.0,
    val City: String = "",
    val State: String = "",
    val Country: String = ""
)
