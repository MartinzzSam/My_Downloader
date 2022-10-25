package com.martinz.mydownloader

import android.app.Application
import com.martinz.mydownloader.presentation.util.DownloaderNotification
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class DownloaderApplication : Application() {

    @Inject
    lateinit var notifications : DownloaderNotification

    override fun onCreate() {
        super.onCreate()
        notifications.createNotificationChannel()
    }



}