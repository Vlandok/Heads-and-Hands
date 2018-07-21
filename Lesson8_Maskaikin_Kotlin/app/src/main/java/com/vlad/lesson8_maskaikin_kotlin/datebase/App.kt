package com.vlad.lesson8_maskaikin_kotlin.datebase

import android.arch.persistence.room.Room
import android.app.Application


class App : Application() {

   lateinit var database: AppDatabase

    companion object {
        lateinit var instance: App
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "mydatabase")
                .allowMainThreadQueries()
                .build()
    }


}