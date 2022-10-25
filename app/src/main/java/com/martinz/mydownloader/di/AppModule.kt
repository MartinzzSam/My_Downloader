package com.martinz.mydownloader.di

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import com.martinz.mydownloader.presentation.util.DownloaderNotification
import com.martinz.mydownloader.presentation.util.MyDownloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideNotificationManger(@ApplicationContext context: Context) : NotificationManager = context.getSystemService(NotificationManager::class.java) as NotificationManager

    @Provides
    @Singleton
    fun provideDownloadManager(@ApplicationContext context: Context) : DownloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

    @Provides
    @Singleton
    fun provideDownloaderNotification(
        @ApplicationContext context : Context,
        notificationManager : NotificationManager
    ) : DownloaderNotification = DownloaderNotification(context , notificationManager)

    @Provides
    @Singleton
    fun provideMyDownloaderManager(downloadManager: DownloadManager) : MyDownloader = MyDownloader(downloadManager)


}