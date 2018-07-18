package com.vlad.lesson7_maskaikin_kotlin

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.text.Html
import com.vlad.lesson7_maskaikin_kotlin.fragments.FragmentList
import kotlinx.android.synthetic.main.activity_2.*
import android.view.View
import com.bumptech.glide.Glide
import com.vlad.lesson7_maskaikin_kotlin.adapters.RVAdapter
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object
import kotlinx.android.synthetic.main.bridge_view.*
import android.support.v7.app.AlertDialog
import android.widget.NumberPicker
import com.vlad.lesson7_maskaikin_kotlin.adapters.RVAdapter.Companion.formatter
import android.widget.TextView


class Activity2 : AppCompatActivity() {

    private lateinit var nm: NotificationManager
    private lateinit var am: AlarmManager
    private lateinit var intent1: Intent
    private lateinit var intent2: Intent
    private lateinit var pIntent1: PendingIntent
    private lateinit var pIntent2: PendingIntent


    companion object {

        const val BASE_URL_BRIDGE = "http://gdemost.handh.ru/"
        const val CHECK_ALARM = "check"
        const val ID_BRIDGE = "id"
        const val ID_BRIDGE_FOR_RECEIVER = "id_for_receiver"
        const val NAME_BRIDGE = "name"
        const val NAME_BRIDGE_CANCEL = "name_cancel"
        const val TEXT_NOTIF = "msgText"
        const val SIZE_ARRAY_PICKER = 16
        const val STEP_PICKER = 15
        const val ONE_HOUR_IN_MINUTE = 60
        const val ONE_MINUTE_IN_MILLISECONDS = 60000
        const val QUARTER_HOUR_IN_MINUTE = 15
        const val ACTION_NOTIFY = "action_notify_bridge"
        const val ACTION_NOTIFY_CANCEL = "action_notify_bridge_сancel"
        const val TEXT_FOR_VIEW_ALARM = "text_view_alarm"

        val NUMS_PICKER_FOUR_HOUR = 15..240
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        toolbar.title = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        am = getSystemService(ALARM_SERVICE) as AlarmManager

        toolbar.setPadding(0, 22, 0, 0)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { _ ->
            finish()
        }

        val bridge: Object = intent.getParcelableExtra(FragmentList.BRIDGE)
        //var checkAlarm: Boolean = intent.getBooleanExtra(FragmentList.CHECK_ALARM, false)
        val text = intent.getStringExtra(FragmentList.TEXT_FOR_VIEW_ALARM)



        if (getImageStatusBridge(bridge) == R.drawable.ic_brige_late) {
            Glide.with(this).load(BASE_URL_BRIDGE + bridge.photoClose).into(imageViewBridge)
        } else {
            Glide.with(this).load(BASE_URL_BRIDGE + bridge.photoOpen).into(imageViewBridge)
        }

        if (bridge.checkAlarm) {
            textViewSetAlarm.text = text
        }

        textViewNameBridge.text = bridge.name?.replace(" мост", "")?.replace("Мост ", "")
        imageViewStatusBridge.setImageDrawable(ContextCompat.getDrawable(applicationContext, getImageStatusBridge(bridge)))
        textViewCloseTimeBridge.text = getTimeCloseBridge(bridge)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewDescriptionBridge.text = Html.fromHtml(bridge.description, 0)
        }

        val arrayString: Array<String> = Array(SIZE_ARRAY_PICKER) { "default" }
        val nums = NUMS_PICKER_FOUR_HOUR
        for ((i, value) in (nums step STEP_PICKER).withIndex()) {
            if (i >= 3) {
                val hour: Int = value / ONE_HOUR_IN_MINUTE
                val minute: Int = value % ONE_HOUR_IN_MINUTE
                if (hour >= 2) {
                    arrayString[i] = "$hour часа $minute мин"
                } else {
                    arrayString[i] = "$hour час $minute мин"
                }
            } else {
                arrayString[i] = "$value мин"
            }

        }


        constraintLayoutClickAlarm.setOnClickListener { _ ->

            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
            val textViewDialogTitle = dialogView.findViewById<TextView>(R.id.textViewDialogTitle)
            val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberPicker)

            numberPicker.minValue = 0
            numberPicker.maxValue = arrayString.size - 1
            numberPicker.value = 1
            numberPicker.displayedValues = arrayString

            textViewDialogTitle.text = bridge.name

            dialog.setView(dialogView)
                    .setPositiveButton(R.string.positive_button_alert) { _: DialogInterface?, _: Int -> }
                    .setNegativeButton(R.string.negative_button_alert) { dialog, _ -> dialog.cancel() }
            val customDialog = dialog.create()
            customDialog.show()

            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { _ ->

                //  время, за сколько нужно предупредить о разводке мостов в минутах
                val timeInMinute = numberPicker.value * QUARTER_HOUR_IN_MINUTE + QUARTER_HOUR_IN_MINUTE

                // время, за сколько нужно предупредить о разводке мостов в милисекундах
                val timeInMiliseconds: Int = timeInMinute * ONE_MINUTE_IN_MILLISECONDS

                val numberTimeDivorse = getNearestTimeCloseBridge(bridge)

                val nearDivorce = formatter.format(bridge.divorces?.get(numberTimeDivorse)?.start) +
                        " - " + formatter.format(bridge.divorces?.get(numberTimeDivorse)?.end) + ""

                val msgText = "${bridge.name} разведется в: $nearDivorce} "

                intent1 = createIntent(msgText, bridge.name, bridge.id)
                pIntent1 = PendingIntent.getBroadcast(this, 0, intent1, 0)

                bridge.checkTimeBeforeStartInMinute = timeInMinute

                am = getSystemService(ALARM_SERVICE) as AlarmManager
                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() , pIntent1)
              //  + getTimeBeforeStart(bridge) - timeInMiliseconds
                startActivityMainWithBooleanAndFlag(bridge, true, getTextForTextViewSetAlarm(timeInMinute))
                customDialog.dismiss()
            }

            if (bridge.checkAlarm) {
                customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener { _ ->
                    intent2 = createIntent(bridge.name, bridge.id)
                    sendBroadcast(intent2)

                    startActivityMainWithBooleanAndFlag(bridge, false)
                    customDialog.dismiss()

                }
            }
        }
    }

    private fun startActivityMainWithBooleanAndFlag(bridge: Object, checkAlarm: Boolean, text: String = getString(R.string.reminder)) {
        val intentCheckAlarm = Intent(this, MainActivity::class.java)
        intentCheckAlarm.putExtra(ID_BRIDGE, bridge.id)
        intentCheckAlarm.putExtra(CHECK_ALARM, checkAlarm)
        intentCheckAlarm.putExtra(TEXT_FOR_VIEW_ALARM, text)
        intentCheckAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intentCheckAlarm)
        overridePendingTransition(0, 0)
    }

    private fun getTimeCloseBridge(bridge: Object): String {
        var index = 0
        var divorse = ""

        while (index < bridge.divorces?.size ?: 1) {
            divorse = formatter.format(bridge.divorces?.get(index)?.start) +
                    " - " + formatter.format(bridge.divorces?.get(index)?.end) + "  $divorse"
            index++
        }
        return divorse
    }

    private fun getTextForTextViewSetAlarm(timeInMinute: Int): String {
        val hour = timeInMinute / ONE_HOUR_IN_MINUTE
        val minute: Int = timeInMinute % ONE_HOUR_IN_MINUTE
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
    private fun getTimeBeforeStart(bridge: Object): Int {

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
        return (hoursBeforeStart * ONE_HOUR_IN_MINUTE + minutesBeforeStart) * ONE_MINUTE_IN_MILLISECONDS

    }

    //находим ближайшее время развводки мостов
    private fun getNearestTimeCloseBridge(bridge: Object): Int {

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

    private fun getImageStatusBridge(bridge: Object): Int {

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

    private fun createIntent(msgText: String, nameBridge: String?, idBridge: Int?): Intent {
        val intent = Intent(this, Receiver::class.java)
        intent.action = ACTION_NOTIFY

        intent.putExtra(TEXT_NOTIF, msgText)
        intent.putExtra(NAME_BRIDGE, nameBridge)
        intent.putExtra(ID_BRIDGE_FOR_RECEIVER, idBridge)
        return intent
    }

    private fun createIntent(nameBridge: String?, idBridge: Int?): Intent {
        val intent = Intent(this, Receiver::class.java)
        intent.action = ACTION_NOTIFY_CANCEL

        intent.putExtra(NAME_BRIDGE_CANCEL, nameBridge)
        intent.putExtra(ID_BRIDGE_FOR_RECEIVER, idBridge)
        return intent
    }


//    fun getStatusBarHeight(): Int {
//        var result = 0
//        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
//        if (resourceId > 0) {
//            result = resources.getDimensionPixelSize(resourceId)
//        }
//        return result
//    }
}
