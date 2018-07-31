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
    private val getWeatherLocalBinder = GetWeatherLocalBinder()
    private lateinit var serviceCallbacks: ServiceCallbacks
    private lateinit var timer: Timer
    private var tTask: TimerTask? = null
    private var interval: Long = ONE_MINUTE_IN_MILLISEC.toLong()

    companion object {

        const val ONE_MINUTE_IN_MILLISEC = 60000

        fun createStartService(context: Context): Intent {
            return Intent(context, GetWeatherLocalService::class.java)
        }
    }

    interface ServiceCallbacks {
        fun getTemperature(temperature: Double?)
    }

    override fun onCreate() {
        Log.d(MY_LOG, "создался")
        super.onCreate()
    }

    override fun onBind(p0: Intent?): IBinder {
        Log.d(MY_LOG, "Бинд!")

        val retrofit = RetrofitGetWeather.instance()
        jsonApi = retrofit.create(WeatherService::class.java)
        timer = Timer()
        schedule()

        return getWeatherLocalBinder
    }

    override fun onDestroy() {
        Log.d(MY_LOG, "destroy")
        disposable.dispose()
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

    fun schedule() {
        if (tTask != null) {
            tTask!!.cancel()
        }
        if (interval > 0) {
            tTask = object : TimerTask() {
                override fun run() {
                    Log.d(MY_LOG, "run")


                    disposable = jsonApi.getWeather(getString(R.string.saransk), UNITS, WEATHER_MAPS_APP_ID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ weather ->
                                serviceCallbacks.getTemperature(weather.main?.temp)

                                Log.d(MY_LOG, "получили темпеературу")
                            },
                                    { exception ->
                                        Log.e(MY_LOG, "error", exception)
                                    })


                }
            }
            timer.schedule(tTask, 0, interval)
        }
    }

}

