package com.udacity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var pendingIntent: PendingIntent
    private lateinit var notificationTitle: String
    private lateinit var downloadStatus: String
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        createChannel()

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if (radio.checkedRadioButtonId != -1) {
                when (radio.checkedRadioButtonId) {
                    radioGlide.id -> download(GLIDE_URL, "Glide")
                    radioApp.id -> download(APP_URL, "App")
                    radioRetrofit.id -> download(RETROFIT_URL, "Retrofit")

                }
                custom_button.M_buttonState = ButtonState.Clicked
            } else {
                Toast.makeText(this, "Please select Something first", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(id!!))
            if (cursor.moveToFirst())
            {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL)
                {
                    downloadStatus = "Success"
                } else if (status == DownloadManager.STATUS_FAILED)
                {
                    downloadStatus = "Failed"
                }
            }

            if (id == downloadID) {
                Toast.makeText(context, "Download finished", Toast.LENGTH_SHORT).show()
                custom_button.M_buttonState = ButtonState.Completed
                val notificationManager =
                    getSystemService(NotificationManager::class.java) as NotificationManager
                notificationManager.sendNotification("Download Finished",notificationTitle,downloadStatus, context!!)
            }
        }
    }

    private fun download(url: String, title: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(title)
                .setDescription(getString(R.string.app_description))
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        notificationTitle = title

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide"
        private const val RETROFIT_URL = "https://github.com/square/retrofit"
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            channel.enableVibration(true)
            channel.description = "Download Finished"
            channel.setShowBadge(false)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
