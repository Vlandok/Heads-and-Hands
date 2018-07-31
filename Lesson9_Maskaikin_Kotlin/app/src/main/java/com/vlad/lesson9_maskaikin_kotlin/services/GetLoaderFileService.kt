package com.vlad.lesson9_maskaikin_kotlin.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.vlad.lesson9_maskaikin_kotlin.MainActivity.Companion.MY_LOG
import io.reactivex.disposables.Disposable
import java.net.URL
import android.widget.Toast
import com.vlad.lesson9_maskaikin_kotlin.MainActivity
import com.vlad.lesson9_maskaikin_kotlin.R
import com.vlad.lesson9_maskaikin_kotlin.getWeather.Main
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.Single
import java.io.*
import java.net.URLConnection
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class GetLoaderFileService : Service() {

    private lateinit var disposable: Disposable
    private val getLoaderFileBinder = GetLoaderFileBinder()
    private lateinit var notificationManager: NotificationManager
    private lateinit var path: String

    companion object {
        const val REMINER = "reminer"
        const val ID_NOTIFY = 123
        const val FILENAME = "image.zip"
        const val UPDATE_PROGRESS = "update_progress"
        const val PATH_IMAGE = "path"
        const val SIZE_BYTE_ARRAY = 1024

        fun createStartService(context: Context, urlFile: String, const: String): Intent {
            val intent = Intent(context, GetLoaderFileService::class.java)
            intent.putExtra(const, urlFile)
            return intent
        }
    }

    override fun onCreate() {
        Log.d(MY_LOG, "onCreate")
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(MY_LOG, "onStartCommand")
        showNotification()

        val urlToDownload = intent?.getStringExtra(MainActivity.URL_CONST)

        val intentBroadcastReceiver = Intent()
        intentBroadcastReceiver.action = MainActivity.BROADCAST_ACTION

        disposable = Single.fromCallable<Any> {
            unpackZip(downloadFile(urlToDownload, intentBroadcastReceiver), FILENAME)
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { str ->
                    path = str.toString()
                    Log.d(MY_LOG, "done")
                    intentBroadcastReceiver.putExtra(PATH_IMAGE, path)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcastReceiver)
                    Toast.makeText(this, getString(R.string.done), Toast.LENGTH_SHORT).show()
                    stopSelf()
                }

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(MY_LOG, "on Bind ")
        return getLoaderFileBinder
    }

    override fun onDestroy() {
        Log.d(MY_LOG, "on Destroy LoadService ")
        disposable.dispose()
        super.onDestroy()
    }

    inner class GetLoaderFileBinder : Binder()

    private fun downloadFile(urlFile: String?, intentBroadcastReceiver: Intent): String {
        try {
            val url = URL(urlFile)
            val connection: URLConnection = url.openConnection()
            connection.connect()
            // this will be useful so that you can show a typical 0-100% progress bar
            val fileLength = connection.contentLength

            // download the file
            val input: InputStream = BufferedInputStream(connection.getInputStream())
            val output: OutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE)
            val data = ByteArray(SIZE_BYTE_ARRAY)
            var total: Long = 0
            var count: Int = MainActivity.DEFAULT_VALUE
            while (input.read(data).let { count = it; it != MainActivity.DEFAULT_VALUE }) {
                total += count
                intentBroadcastReceiver.putExtra(UPDATE_PROGRESS, (total * MainActivity.MAX_PROGRESS / fileLength).toInt())
                LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcastReceiver)
                output.write(data, 0, count)
            }

            output.flush()
            output.close()
            input.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        intentBroadcastReceiver.putExtra(UPDATE_PROGRESS, MainActivity.MAX_PROGRESS)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcastReceiver)

        return "$filesDir"
    }

    private fun showNotification() {

        val text = getText(R.string.downloading_file)

        val contentIntent = PendingIntent.getActivity(this, 0,
                Intent(this, MainActivity::class.java), 0)

        val notification = NotificationCompat.Builder(this, REMINER)
                .setSmallIcon(R.drawable.ic_small_brige)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.downloading))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setProgress(MainActivity.MAX_PROGRESS, 0, true)
                .build()

        notificationManager.notify(ID_NOTIFY, notification)
        startForeground(ID_NOTIFY, notification)
    }

    private fun unpackZip(path: String, zipName: String): String {
        val inputStream: InputStream
        val zipInputStream: ZipInputStream
        lateinit var filename: String
        try {
            inputStream = FileInputStream("$path/$zipName")
            zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
            var zipEntry: ZipEntry? = null
            val buffer = ByteArray(SIZE_BYTE_ARRAY)
            var count: Int = MainActivity.DEFAULT_VALUE
            while (zipInputStream.nextEntry.let { zipEntry = it; it != null }) {
                filename = zipEntry!!.name
                if (zipEntry!!.isDirectory) {
                    val fmd = File("$path/$filename")
                    fmd.mkdirs()
                    continue
                }
                val fileOutputStream = FileOutputStream("$path/$filename")
                while (zipInputStream.read(buffer).let { count = it; it != MainActivity.DEFAULT_VALUE }) {
                    fileOutputStream.write(buffer, 0, count)
                }
                fileOutputStream.close()
                zipInputStream.closeEntry()
            }

            zipInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return "$path/$filename"
    }


}