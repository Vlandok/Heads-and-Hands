package com.vlad.lesson9_maskaikin_kotlin.retrofit

import com.vlad.lesson9_maskaikin_kotlin.getWeather.ResultWeather
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    fun getWeather(@Query("q") city: String, @Query("units") units: String, @Query("APPID") APPID: String): Observable<ResultWeather>

}