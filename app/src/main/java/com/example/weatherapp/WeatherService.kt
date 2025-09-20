package com.example.weatherapp

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService{

    @GET("current.json")
    suspend fun getWeather(
        @Query("key") apikey: String,
        @Query("q") city: String): Response<WeatherResponse>

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") apikey: String,
        @Query("q") city: String): Response<WeatherResponse>
}

object RetrofitIObj{
    private val retrofit_initiator = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInstance = retrofit_initiator.create(WeatherService::class.java)
}
object RetrofitIObj2{
    private val retrofit_initiator = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInstance2 = retrofit_initiator.create(WeatherService::class.java)
}
