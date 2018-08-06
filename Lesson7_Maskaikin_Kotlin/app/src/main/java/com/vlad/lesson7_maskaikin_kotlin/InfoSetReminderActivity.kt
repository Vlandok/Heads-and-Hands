package com.vlad.lesson7_maskaikin_kotlin

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Html
import com.vlad.lesson7_maskaikin_kotlin.fragments.FragmentViewListBridge
import kotlinx.android.synthetic.main.activity_info_set_reminder.*
import android.view.View
import com.bumptech.glide.Glide
import com.vlad.lesson7_maskaikin_kotlin.adapters.RVAdapter
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object
import kotlinx.android.synthetic.main.bridge_view.*
import android.support.v7.app.AlertDialog
import android.widget.NumberPicker
import com.vlad.lesson7_maskaikin_kotlin.adapters.RVAdapter.Companion.formatter
import android.widget.TextView
import android.app.PendingIntent
import android.app.AlarmManager


class InfoSetReminderActivity : AppCompatActivity() {

    private lateinit var nm: NotificationManager
    private lateinit var am: AlarmManager

    private lateinit var intentAlarm: Intent

    private lateinit var pendingIntent: PendingIntent


    companion object {

        const val BASE_URL_BRIDGE = "http://gdemost.handh.ru/"
        const val CHECK_ALARM = "check"
        const val ID_BRIDGE = "id"
        const val ID_BRIDGE_FOR_RECEIVER = "id_for_receiver"
        const val NAME_BRIDGE = "name"
        const val TEXT_NOTIF = "msgText"
        const val SIZE_ARRAY_PICKER = 16
        const val STEP_PICKER = 15
        const val ONE_HOUR_IN_MINUTE = 60
        const val ONE_MINUTE_IN_MILLISECONDS = 60000
        const val QUARTER_HOUR_IN_MINUTE = 15
        const val ACTION_NOTIFY = "action_notify_bridge"
        const val TEXT_FOR_VIEW_ALARM = "text_view_alarm"

        const val BRIDGE = "bridge"

        val NUMS_PICKER_FOUR_HOUR = 15..240

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_set_reminder)

        toolbar.title = getString(R.string.replace_empty)

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

        val bridge: Object = intent.getParcelableExtra(FragmentViewListBridge.BRIDGE)
        //    val checkAlarm: Boolean = intent.getBooleanExtra(FragmentViewListBridge.CHECK_ALARM, false)


        if (getImageStatusBridge(bridge) == R.drawable.ic_brige_late) {
            Glide.with(this).load(BASE_URL_BRIDGE + bridge.photoClose).into(imageViewBridge)
        } else {
            Glide.with(this).load(BASE_URL_BRIDGE + bridge.photoOpen).into(imageViewBridge)
        }

        if (bridge.checkAlarm) {
            textViewSetAlarm.text = getString(R.string.isEstablished)
        }

        textViewNameBridge.text = bridge.name?.replace(getString(R.string.replace_bridge), getString(R.string.replace_empty))
                ?.replace(getString(R.string.replace_Bridge), getString(R.string.replace_empty))
        imageViewStatusBridge.setImageDrawable(ContextCompat.getDrawable(applicationContext, getImageStatusBridge(bridge)))
        textViewCloseTimeBridge.text = getTimeCloseBridge(bridge)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewDescriptionBridge.text = Html.fromHtml(bridge.description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textViewDescriptionBridge.text = Html.fromHtml(bridge.description)
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

                intentAlarm = createIntent(bridge.id, bridge)
                pendingIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT)

                bridge.checkTimeBeforeStartInMinute = timeInMinute

                am = getSystemService(ALARM_SERVICE) as AlarmManager
                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + getTimeBeforeStart(bridge) - timeInMiliseconds, pendingIntent)

                bridge.checkAlarm = true
                startActivityMainWithBooleanAndFlag(bridge, true)
                customDialog.dismiss()
            }

            if (bridge.checkAlarm) {
                customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener { _ ->

                    val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                    intentAlarm = createIntent(bridge.id, bridge)
                    pendingIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.cancel(pendingIntent)
                    pendingIntent.cancel()

                    bridge.checkAlarm = false
                    startActivityMainWithBooleanAndFlag(bridge, false)

                    customDialog.dismiss()

                }
            }
        }
    }

    private fun startActivityMainWithBooleanAndFlag(bridge: Object, checkAlarm: Boolean) {
        val intentCheckAlarm = Intent(this, MainActivity::class.java)
        intentCheckAlarm.putExtra(ID_BRIDGE, bridge.id)
        intentCheckAlarm.putExtra(CHECK_ALARM, checkAlarm)
        intentCheckAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intentCheckAlarm)
        overridePendingTransition(0, 0)
    }

    private fun createIntent (idBridge: Int?, bridge: Object): Intent {
        val intent = Intent(this, Receiver::class.java)
        intent.action = idBridge.toString()
        intent.putExtra(BRIDGE, bridge)
        return intent
    }
}
