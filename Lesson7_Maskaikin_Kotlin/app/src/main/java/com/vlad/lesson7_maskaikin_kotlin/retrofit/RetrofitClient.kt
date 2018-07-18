package com.vlad.lesson7_maskaikin_kotlin.retrofit

import android.provider.SyncStateContract
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson



private val RETROFIT_GET_BRIDGE_URL = "http://gdemost.handh.ru/"

object RetrofitClient {
    private var outInstance: Retrofit? = null

    val instance:Retrofit
    get() {
        if (outInstance == null)
        {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val gson = GsonBuilder()
                    .setDateFormat("HH:mm:ss")
                    .create()

            outInstance = Retrofit.Builder()
                    .baseUrl(RETROFIT_GET_BRIDGE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        }
        return outInstance!!
    }
}