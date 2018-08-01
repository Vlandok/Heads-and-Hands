package com.vlad.lesson9_maskaikin_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import com.vlad.lesson9_maskaikin_kotlin.services.*
import kotlinx.android.synthetic.main.activity_main.*
import android.content.*
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.FileInputStream


class MainActivity : AppCompatActivity(), GetWeatherLocalService.ServiceCallbacks {

    private var getWeatherService = GetWeatherLocalService()
    private lateinit var broadCastReceiver : BroadcastReceiver

    companion object {
        const val RETROFIT_GET_WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/"
        const val WEATHER_MAPS_APP_ID = "a924f0f5b30839d1ecb95f0a6416a0c2"
        const val UNITS = "metric"
        const val CELSIUS = "°C"
        const val MY_LOG = "My_Log"
        const val BROADCAST_ACTION = "com.vlad.lesson9_maskaikin_kotlin.BroadCastService"
        const val URL_CONST = "url"
        const val URL_FILE = "https://getfile.dokpub.com/yandex/get/https://yadi.sk/d/TgvhADAW3ZbguD"
        const val DEFAULT_VALUE = -1
        const val MAX_PROGRESS = 100
        const val WIDTH_HEIGHT_IMAGE = 250
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(GetWeatherLocalService.createStartService(this), mConnection, Context.BIND_AUTO_CREATE)

        broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context, intent: Intent?) {

                val progress = intent?.getIntExtra(GetLoaderFileService.UPDATE_PROGRESS, DEFAULT_VALUE)
                if (progress != DEFAULT_VALUE) {
                    Log.d(MY_LOG, "Прогрес: $progress")
                    progressBarGetImage.visibility = View.VISIBLE
                    if (progress == MAX_PROGRESS && intent.getStringExtra(GetLoaderFileService.PATH_IMAGE) != null) {
                        progressBarGetImage.visibility = View.GONE

                        val inputStream = FileInputStream(intent.getStringExtra(GetLoaderFileService.PATH_IMAGE))
                        Single.fromCallable {
                            BitmapFactory.decodeStream(inputStream)
                        }
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { bitmap ->
                                    imageViewFromInternet.setImageBitmap(Bitmap.createScaledBitmap(bitmap, WIDTH_HEIGHT_IMAGE, WIDTH_HEIGHT_IMAGE, false))
                                }
                    }
                }
            }
        }

        buttonLoadFile.setOnClickListener {
            startService(GetLoaderFileService.createStartService(this, URL_FILE, URL_CONST))
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, IntentFilter(BROADCAST_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver)
    }


    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {

            Log.d(MY_LOG, "coonect")
            val binder = service as GetWeatherLocalService.GetWeatherLocalBinder
            getWeatherService = binder.getService()
            getWeatherService.setCallbacks(this@MainActivity)

        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(MY_LOG, "отключился")
        }
    }

    override fun showTemperature(temperature: Double?) {
        Log.d(MY_LOG, "Установили погоду")
        if (temperature == DEFAULT_VALUE.toDouble()) {
            textViewTemperature.text = "Не получилось получить температуру"
        } else {
            textViewTemperature.text = "${temperature?.toInt()}$CELSIUS"
        }
        progressBarGetWeather.visibility = View.GONE
    }

}
