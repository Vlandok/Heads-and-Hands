package com.vlad.lesson7_maskaikin_kotlin

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log


class Receiver : BroadcastReceiver() {

    companion object {
        private const val REMINER = "Напоминание"
        private const val ID_NOTIFY = 123
    }

    override fun onReceive(ctx: Context, intent: Intent) {

        val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (intent.action == Activity2.ACTION_NOTIFY_CANCEL) {
            notificationManager.cancel(ID_NOTIFY)
          //  notificationManager.cancel(intent.getIntExtra(Activity2.ID_BRIDGE_FOR_RECEIVER, -1))

        } else {
            createNotification(ctx, notificationManager, REMINER, intent.getStringExtra(Activity2.TEXT_NOTIF),
                    intent.getStringExtra(Activity2.NAME_BRIDGE), intent.getIntExtra(Activity2.ID_BRIDGE_FOR_RECEIVER, -1))
        }
    }

    fun createNotification(context: Context, notificationManager: NotificationManager, msg: String,
                           msgText: String, msgAlert: String, idBridge: Int) {

        val newIntent = Intent(context, MainActivity::class.java)

        val notificatonIntent = PendingIntent.getActivity(context, 0, newIntent, 0)
        val mBuilder = NotificationCompat.Builder(context, REMINER)
                .setSmallIcon(R.drawable.ic_brige_normal)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(notificatonIntent)
                .setContentText(msgText)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setAutoCancel(true)

        notificationManager.notify(ID_NOTIFY, mBuilder.build())
    }

}