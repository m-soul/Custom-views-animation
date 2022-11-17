package com.udacity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import androidx.core.app.NotificationCompat

const val channelId = "DownloadChannelId"
const val channelName = "DownloadChannel"
private const val NOTIFICATION_ID = 1
private const val REQUEST_CODE = 1
fun NotificationManager.sendNotification(message: String,title: String,downloadStatus: String, context: Context) {
    val intent = Intent(context, DetailActivity::class.java)
    intent.putExtra("title", title)
    intent.putExtra("downloadStatus", downloadStatus)
    val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    val builder = NotificationCompat.Builder(
        context,
        channelId
    ).setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.ic_assistant_black_24dp).addAction(R.drawable.ic_assistant_black_24dp,"check status", pendingIntent).setAutoCancel(true)
    notify(NOTIFICATION_ID, builder.build())
}