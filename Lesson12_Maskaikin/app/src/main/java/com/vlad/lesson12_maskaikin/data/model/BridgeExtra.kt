package com.vlad.lesson12_maskaikin.data.model

import android.annotation.SuppressLint
import com.vlad.lesson12_maskaikin.R
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
val formatter = SimpleDateFormat("HH:mm")
@SuppressLint("SimpleDateFormat")
val formatterHour = SimpleDateFormat("HH")
@SuppressLint("SimpleDateFormat")
val formatterMinute = SimpleDateFormat("mm")

const val ONE_HOUR_IN_MINUTE = 60

fun getTimeCloseBridge(bridge: Bridge): String {

    val getDivorceSize = bridge.divorces?.size
    var index = 0
    var divorse = ""

    while (index < getDivorceSize!!) {
        divorse = formatter.format(bridge.divorces?.get(index)?.start) +
                " - " + formatter.format(bridge.divorces?.get(index)?.end) + "  $divorse"
        index++
    }
    return divorse
}

fun getImageStatusBridge(bridge: Bridge): Int {

    val getTimeNow = System.currentTimeMillis()
    val timeNowForrmatedHour = formatterHour.format(getTimeNow).toInt()
    val timeNowForrmatedMinute = formatterMinute.format(getTimeNow).toInt()

    val timeNowInMinute = timeNowForrmatedHour * ONE_HOUR_IN_MINUTE + timeNowForrmatedMinute

    val getDivorceSize = bridge.divorces?.size
    var index = 0
    var resultImg: Int = -1

    while (index < getDivorceSize!!) {

        val startTimeCloseBridgeHour = formatterHour.format(bridge.divorces?.get(index)?.start).toInt()
        val endTimeCloseBridgeHour = formatterHour.format(bridge.divorces?.get(index)?.end).toInt()
        val startTimeCloseBridgeMinute = formatterMinute.format(bridge.divorces?.get(index)?.start).toInt()
        val endTimeCloseBridgeMinute = formatterMinute.format(bridge.divorces?.get(index)?.end).toInt()

        val startTimeCloseBridgeInMinute = startTimeCloseBridgeHour * ONE_HOUR_IN_MINUTE + startTimeCloseBridgeMinute
        val endTimeCloseBridgeInMinute = endTimeCloseBridgeHour * ONE_HOUR_IN_MINUTE + endTimeCloseBridgeMinute

        if (timeNowInMinute in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
            resultImg = R.drawable.ic_brige_late
            break
        } else if (timeNowInMinute + ONE_HOUR_IN_MINUTE in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
            resultImg = R.drawable.ic_brige_soon
            break
        } else {
            resultImg = R.drawable.ic_brige_normal
        }
        index++
    }
    return resultImg
}