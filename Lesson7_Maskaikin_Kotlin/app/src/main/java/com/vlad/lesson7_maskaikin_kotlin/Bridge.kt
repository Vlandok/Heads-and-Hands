package com.vlad.lesson7_maskaikin_kotlin

import android.content.Context
import android.content.Intent
import android.util.Log
import com.vlad.lesson7_maskaikin_kotlin.adapters.RVAdapter
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object
import com.vlad.lesson7_maskaikin_kotlin.getBridge.ResultBridge

// Файл с методами для работы с Мостами!

fun getLatBridges(bridges: ResultBridge, countBridge: Int?): DoubleArray? {


    val lat = countBridge?.let { DoubleArray(it) }
    if (countBridge != null) {
        var i = 0
        while (i < countBridge) {
            lat?.set(i, bridges.objects!![i].lat!!.toDouble())
            i++
        }
    }
    return lat
}

fun getLngBridges(bridges: ResultBridge, countBridge: Int?): DoubleArray? {

    val lng = countBridge?.let { DoubleArray(it) }
    if (countBridge != null) {
        var i = 0
        while (i < countBridge) {
            lng?.set(i, bridges.objects!![i].lng!!.toDouble())
            i++
        }
    }
    return lng
}

fun getTimeCloseBridge(position: Int, bridge: Object?): String {

    val getDivorceSize = bridge?.divorces?.size
    var index = 0
    var divorse = ""

    while (index < getDivorceSize!!) {
        divorse = RVAdapter.formatter.format(bridge?.divorces?.get(index)?.start) +
                " - " + RVAdapter.formatter.format(bridge?.divorces?.get(index)?.end) + "  $divorse"
        index++
    }
    return divorse
}

fun getImageStatusAlarm(bridge: Object?): Int {

    return if (bridge?.checkAlarm!!) {
        R.drawable.ic_kolocol_on
    } else {
        R.drawable.ic_kolocol_off
    }
}

fun getImageStatusBridge(position: Int, bridges: ResultBridge): Int {

    val getTimeNow = System.currentTimeMillis()
    val timeNowForrmatedHour = RVAdapter.formatterHour.format(getTimeNow).toInt()
    val timeNowForrmatedMinute = RVAdapter.formatterMinute.format(getTimeNow).toInt()

    val timeNowInMinute = timeNowForrmatedHour * RVAdapter.ONE_HOUR_IN_MINUTE + timeNowForrmatedMinute

    val getDivorceSize = bridges.objects?.get(position)?.divorces?.size
    var index = 0
    var resultImg: Int = -1

    while (index < getDivorceSize!!) {

        val startTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridges.objects?.get(position)?.divorces?.get(index)?.start).toInt()
        val endTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridges.objects?.get(position)?.divorces?.get(index)?.end).toInt()
        val startTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridges.objects?.get(position)?.divorces?.get(index)?.start).toInt()
        val endTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridges.objects?.get(position)?.divorces?.get(index)?.end).toInt()

        val startTimeCloseBridgeInMinute = startTimeCloseBridgeHour * RVAdapter.ONE_HOUR_IN_MINUTE + startTimeCloseBridgeMinute
        val endTimeCloseBridgeInMinute = endTimeCloseBridgeHour * RVAdapter.ONE_HOUR_IN_MINUTE + endTimeCloseBridgeMinute

        if (timeNowInMinute in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
            resultImg = R.drawable.ic_brige_late
            break
        } else if (timeNowInMinute + RVAdapter.ONE_HOUR_IN_MINUTE in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
            resultImg = R.drawable.ic_brige_soon
            break
        } else {
            resultImg = R.drawable.ic_brige_normal
        }
        index++
    }
    return resultImg
}

fun getImageStatusBridgeForMap(position: Int, bridges: ResultBridge): Int {

    val getTimeNow = System.currentTimeMillis()
    val timeNowForrmatedHour = RVAdapter.formatterHour.format(getTimeNow).toInt()
    val timeNowForrmatedMinute = RVAdapter.formatterMinute.format(getTimeNow).toInt()

    val timeNowInMinute = timeNowForrmatedHour * RVAdapter.ONE_HOUR_IN_MINUTE + timeNowForrmatedMinute

    val getDivorceSize = bridges.objects?.get(position)?.divorces?.size
    var index = 0
    var resultImg: Int = -1

    while (index < getDivorceSize!!) {

        val startTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridges.objects?.get(position)?.divorces?.get(index)?.start).toInt()
        val endTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridges.objects?.get(position)?.divorces?.get(index)?.end).toInt()
        val startTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridges.objects?.get(position)?.divorces?.get(index)?.start).toInt()
        val endTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridges.objects?.get(position)?.divorces?.get(index)?.end).toInt()

        val startTimeCloseBridgeInMinute = startTimeCloseBridgeHour * RVAdapter.ONE_HOUR_IN_MINUTE + startTimeCloseBridgeMinute
        val endTimeCloseBridgeInMinute = endTimeCloseBridgeHour * RVAdapter.ONE_HOUR_IN_MINUTE + endTimeCloseBridgeMinute

        if (timeNowInMinute in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
            resultImg = R.drawable.ic_brige_late_2
            break
        } else if (timeNowInMinute + RVAdapter.ONE_HOUR_IN_MINUTE in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
            resultImg = R.drawable.ic_brige_soon_2
            break
        } else {
            resultImg = R.drawable.ic_brige_normal_2
        }
        index++
    }
    return resultImg
}


fun getTimeCloseBridge(bridge: Object): String {
    var index = 0
    var divorse = ""

    while (index < bridge.divorces?.size ?: 1) {
        divorse = RVAdapter.formatter.format(bridge.divorces?.get(index)?.start) +
                " - " + RVAdapter.formatter.format(bridge.divorces?.get(index)?.end) + "  $divorse"
        index++
    }
    return divorse
}

fun getTextForTextViewSetAlarm(timeInMinute: Int): String {
    val hour = timeInMinute / InfoSetReminderActivity.ONE_HOUR_IN_MINUTE
    val minute: Int = timeInMinute % InfoSetReminderActivity.ONE_HOUR_IN_MINUTE
    val text: String
    if (hour >= 2) {
        text = "За $hour часа $minute мин"
    } else if (hour in 1..2) {
        text = "За $hour час $minute мин"
    } else {
        text = "За $timeInMinute минут"
    }
    return text
}


//Метод получает, сколько миллисекунд осталось с этого времени до разводки моста
fun getTimeBeforeStart(bridge: Object): Int {

    val getTimeNow = System.currentTimeMillis()
    val timeNowForrmatedHour = RVAdapter.formatterHour.format(getTimeNow).toInt()
    val timeNowForrmatedMinute = RVAdapter.formatterMinute.format(getTimeNow).toInt()

    val startTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridge.divorces?.get(0)?.start).toInt()
    val startTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridge.divorces?.get(0)?.start).toInt()

    val hoursBeforeStart: Int
    val minutesBeforeStart: Int

    getNearestTimeCloseBridge(bridge)

    if (timeNowForrmatedMinute > startTimeCloseBridgeMinute) {
        minutesBeforeStart = 60 - timeNowForrmatedMinute + startTimeCloseBridgeMinute
        if (timeNowForrmatedHour > startTimeCloseBridgeHour) {
            hoursBeforeStart = 24 - timeNowForrmatedHour + startTimeCloseBridgeHour - 1
        } else {
            hoursBeforeStart = startTimeCloseBridgeHour - timeNowForrmatedHour - 1
        }
    } else {
        minutesBeforeStart = startTimeCloseBridgeMinute - timeNowForrmatedMinute
        if (timeNowForrmatedHour > startTimeCloseBridgeHour) {
            hoursBeforeStart = 24 - timeNowForrmatedHour + startTimeCloseBridgeHour
        } else {
            hoursBeforeStart = startTimeCloseBridgeHour - timeNowForrmatedHour
        }

    }
    return (hoursBeforeStart * InfoSetReminderActivity.ONE_HOUR_IN_MINUTE + minutesBeforeStart) * InfoSetReminderActivity.ONE_MINUTE_IN_MILLISECONDS

}

//находим ближайшее время развводки мостов
fun getNearestTimeCloseBridge(bridge: Object): Int {

    val getTimeNow = System.currentTimeMillis()
    val timeNowForrmatedHour = RVAdapter.formatterHour.format(getTimeNow).toInt()
    val timeNowForrmatedMinute = RVAdapter.formatterMinute.format(getTimeNow).toInt()

    var startTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridge.divorces?.get(0)?.start).toInt()
    var startTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridge.divorces?.get(0)?.start).toInt()

    val getDivorceSize = bridge.divorces?.size
    var index = 1
    var numerDivroseTime = 0

    while (index < getDivorceSize!!) {
        if (startTimeCloseBridgeHour > RVAdapter.formatterHour.format(bridge.divorces?.get(index)?.start).toInt()) {
            if (timeNowForrmatedHour - RVAdapter.formatterHour.format(bridge.divorces?.get(index)?.start).toInt() <= 0) {
                if (timeNowForrmatedHour - RVAdapter.formatterHour.format(bridge.divorces?.get(index)?.start).toInt() == 0
                        && timeNowForrmatedMinute < RVAdapter.formatterMinute.format(bridge.divorces?.get(index)?.start).toInt()) {
                    startTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridge.divorces?.get(index)?.start).toInt()
                    startTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridge.divorces?.get(index)?.start).toInt()
                    numerDivroseTime = 1
                }
            }
        } else if (timeNowForrmatedHour >= startTimeCloseBridgeHour
                && timeNowForrmatedHour < RVAdapter.formatterHour.format(bridge.divorces?.get(index)?.start).toInt()) {
            if (timeNowForrmatedHour == startTimeCloseBridgeHour && timeNowForrmatedMinute > startTimeCloseBridgeMinute) {
                startTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridge.divorces?.get(index)?.start).toInt()
                startTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridge.divorces?.get(index)?.start).toInt()
                numerDivroseTime = 1
            }
        }
        index++
    }

    return numerDivroseTime

}

fun getImageStatusBridge(bridge: Object): Int {

    val getTimeNow = System.currentTimeMillis()
    val timeNowForrmatedHour = RVAdapter.formatterHour.format(getTimeNow).toInt()
    val timeNowForrmatedMinute = RVAdapter.formatterMinute.format(getTimeNow).toInt()

    val timeNowInMinute = timeNowForrmatedHour * RVAdapter.ONE_HOUR_IN_MINUTE + timeNowForrmatedMinute

    val getDivorceSize = bridge.divorces?.size
    var index = 0
    var resultImg: Int = R.drawable.ic_brige_normal

    while (index < getDivorceSize!!) {

        val startTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridge.divorces?.get(index)?.start).toInt()
        val endTimeCloseBridgeHour = RVAdapter.formatterHour.format(bridge.divorces?.get(index)?.end).toInt()
        val startTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridge.divorces?.get(index)?.start).toInt()
        val endTimeCloseBridgeMinute = RVAdapter.formatterMinute.format(bridge.divorces?.get(index)?.end).toInt()

        val startTimeCloseBridgeInMinute = startTimeCloseBridgeHour * RVAdapter.ONE_HOUR_IN_MINUTE + startTimeCloseBridgeMinute
        val endTimeCloseBridgeInMinute = endTimeCloseBridgeHour * RVAdapter.ONE_HOUR_IN_MINUTE + endTimeCloseBridgeMinute

        if (timeNowInMinute in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
            resultImg = R.drawable.ic_brige_late
            break
        } else if (timeNowInMinute + RVAdapter.ONE_HOUR_IN_MINUTE in startTimeCloseBridgeInMinute..endTimeCloseBridgeInMinute) {
            resultImg = R.drawable.ic_brige_soon
            break
        } else {
            resultImg = R.drawable.ic_brige_normal
        }
        index++
    }
    return resultImg
}

fun checkAlarmBridge(bridges: ResultBridge, context: Context?) {
    var i = 0
    while (i < bridges.objects?.size ?: 1) {
        val bridge = bridges.objects?.get(i)
        val intent = Intent(context, Receiver::class.java)
        intent.action = bridge?.id.toString()
        intent.putExtra(InfoSetReminderActivity.BRIDGE, bridge)
        if (AlarmUtils.isAlarm(context, intent, 0)) {
            Log.d(MainActivity.MY_LOG, "Alarm is already active")
            bridge?.checkAlarm = true
        } else {
            bridge?.checkAlarm = false
        }
        i++
    }
}
