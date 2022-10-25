package com.martinz.mydownloader.presentation.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.martinz.mydownloader.Const.DOWNLOAD_COMPLETE
import com.martinz.mydownloader.Const.FILE_NAME
import com.martinz.mydownloader.Const.FILE_STATUS
import com.martinz.mydownloader.R
import com.martinz.mydownloader.presentation.detail_activity.DetailActivity

class DownloaderNotification(
    val context: Context,
    private val notificationManager: NotificationManager
) {


    companion object {
        const val CHANNEL_ID = "channelId"
        const val APP_CHANNEL_NAME = "LoadAppChannel"
        const val REQUEST_CODE = 0
        const val NOTIFICATION_ID = 71213
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                APP_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { setShowBadge(false) }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            notificationChannel.enableVibration(true)
            notificationChannel.description = DOWNLOAD_COMPLETE

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    fun createNotification(title: String, text: String, downloadStatus: String) {

        val detailIntent = Intent(context, DetailActivity::class.java)
        detailIntent.putExtra(FILE_NAME, title)
        detailIntent.putExtra(FILE_STATUS, downloadStatus)


        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            detailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val action = NotificationCompat.Action(
            R.drawable.ic_baseline_notifications_active_24,
            context.getString(R.string.notification_button),
            pendingIntent
        )


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText("$text is $downloadStatus")
            .setContentIntent(pendingIntent)
            .addAction(action)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


}