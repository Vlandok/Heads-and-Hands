package com.vlad.lesson9_maskaikin_kotlin.retrofit

import com.vlad.lesson9_maskaikin_kotlin.MainActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitGetWeather {
fun instance (): Retrofit {

    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val client =OkHttpClient.Builder().addInterceptor(interceptor).build()

    lateinit var retrofitGetWeather: Retrofit
    retrofitGetWeather = Retrofit.Builder()
            .baseUrl(MainActivity.RETROFIT_GET_WEATHER_BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    return retrofitGetWeather
}
}