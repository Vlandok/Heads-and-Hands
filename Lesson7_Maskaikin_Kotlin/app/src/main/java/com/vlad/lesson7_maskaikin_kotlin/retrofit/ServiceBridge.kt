package com.vlad.lesson7_maskaikin_kotlin.retrofit

import com.vlad.lesson7_maskaikin_kotlin.getBridge.ResultBridge
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface ServiceBridge {

    @GET("api/v1/bridges/?format=json")
    fun bridges() : Single<ResultBridge>


}