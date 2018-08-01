package com.vlad.lesson9_maskaikin_kotlin.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.vlad.lesson9_maskaikin_kotlin.MainActivity.Companion.MY_LOG
import com.vlad.lesson9_maskaikin_kotlin.MainActivity.Companion.UNITS
import com.vlad.lesson9_maskaikin_kotlin.MainActivity.Companion.WEATHER_MAPS_APP_ID
import com.vlad.lesson9_maskaikin_kotlin.R
import com.vlad.lesson9_maskaikin_kotlin.retrofit.WeatherService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.vlad.lesson9_maskaikin_kotlin.MainActivity
import com.vlad.lesson9_maskaikin_kotlin.retrofit.RetrofitGetWeather
import java.util.*


class GetWeatherLocalService : Service() {

    private lateinit var jsonApi: WeatherService
    private lateinit var disposable: Disposable
    private lateinit var serviceCallbacks: ServiceCallbacks
    private var tTask: TimerTask? = null
    private var interval: Long = ONE_MINUTE_IN_MILLISEC.toLong()

    companion object {

        const val ONE_MINUTE_IN_MILLISEC = 60000

        fun createStartService(context: Context): Intent {
            return Intent(context, GetWeatherLocalService::class.java)
        }
    }

    interface ServiceCallbacks {
        fun showTemperature(temperature: Double?)
    }

    override fun onCreate() {
        Log.d(MY_LOG, "создался")
        val retrofit = RetrofitGetWeather.instance()
        jsonApi = retrofit.create(WeatherService::class.java)
        val timer = Timer()
        schedule(timer)
        super.onCreate()
    }

    override fun onBind(p0: Intent?): IBinder {
        Log.d(MY_LOG, "Бинд!")
        return GetWeatherLocalBinder()
    }

    override fun onDestroy() {
        Log.d(MY_LOG, "destroy")
        disposable.dispose()
        tTask?.cancel()
        super.onDestroy()
    }

    fun setCallbacks(callbacks: ServiceCallbacks) {
        serviceCallbacks = callbacks
    }

    inner class GetWeatherLocalBinder : Binder() {
        fun getService(): GetWeatherLocalService {
            return this@GetWeatherLocalService
        }
    }

    private fun schedule(timer: Timer) {
        if (tTask != null) {
            tTask?.cancel()
        }
        if (interval > 0) {
            tTask = object : TimerTask() {
                override fun run() {
                    Log.d(MY_LOG, "run")

                    disposable = jsonApi.getWeather(getString(R.string.saransk), UNITS, WEATHER_MAPS_APP_ID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ weather ->
                                if (weather == null){
                                    serviceCallbacks.showTemperature(MainActivity.DEFAULT_VALUE.toDouble())
                                } else {
                                    serviceCallbacks.showTemperature(weather.main?.temp)
                                }

                                Log.d(MY_LOG, "получили темпеературу")
                            },
                                    { exception ->
                                        exception.printStackTrace()
                                    })

                }
            }
            timer.schedule(tTask, 0, interval)
        }
    }

}

