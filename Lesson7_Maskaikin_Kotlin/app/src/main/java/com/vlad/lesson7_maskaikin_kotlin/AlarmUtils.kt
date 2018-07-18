package com.vlad.lesson7_maskaikin_kotlin

import android.app.PendingIntent
import android.content.Context
import android.content.Intent



class AlarmUtils {
    companion object {
        fun hasAlarm(context: Context, intent: Intent, requestCode: Int): Boolean {
            return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null
        }
    }

}