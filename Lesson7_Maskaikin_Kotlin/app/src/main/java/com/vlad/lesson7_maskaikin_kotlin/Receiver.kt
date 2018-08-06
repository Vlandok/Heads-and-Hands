package com.vlad.lesson7_maskaikin_kotlin

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.vlad.lesson7_maskaikin_kotlin.adapters.RVAdapter
import com.vlad.lesson7_maskaikin_kotlin.fragments.FragmentViewListBridge
import com.vlad.lesson7_maskaikin_kotlin.fragments.FragmentViewMapBridge
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object


class Receiver : BroadcastReceiver() {

    companion object {
        private const val REMINER = "Напоминание"
        private const val ID_NOTIFY = 123
    }

    override fun onReceive(ctx: Context, intent: Intent) {

        val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bridge = intent.getParcelableExtra<Object>(InfoSetReminderActivity.BRIDGE)

        val numberTimeDivorse = getNearestTimeCloseBridge(bridge)

        val nearDivorce = RVAdapter.formatter.format(bridge.divorces?.get(numberTimeDivorse)?.start) +
                " - " + RVAdapter.formatter.format(bridge.divorces?.get(numberTimeDivorse)?.end) + ""

        val msgText = "${bridge.name} разведется в: $nearDivorce} "

        createNotification(ctx, notificationManager, REMINER, msgText,bridge )

    }

    fun createNotification(context: Context, notificationManager: NotificationManager, msg: String,
                           msgText: String, bridge: Object) {

        val newIntent = Intent(context, InfoSetReminderActivity::class.java)
        newIntent.putExtra(FragmentViewListBridge.BRIDGE, bridge)

        val notificatonIntent = PendingIntent.getActivity(context, 0, newIntent, 0)
        val mBuilder = NotificationCompat.Builder(context, REMINER)
                .setSmallIcon(R.drawable.ic_brige_normal)
                .setContentTitle(msg)
                .setTicker(bridge.name)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(notificatonIntent)
                .setContentText(msgText)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true)

        notificationManager.notify(ID_NOTIFY, mBuilder.build())
    }

}