package com.martinz.mydownloader.presentation.util

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.net.Uri
import com.martinz.mydownloader.Const
import kotlinx.coroutines.flow.MutableStateFlow

class MyDownloader(
    private val downloadManager: DownloadManager
) {

    var downloadId : MutableStateFlow<Long> = MutableStateFlow<Long>(0)

     fun download(appURL: AppURL) {
        val request =
            DownloadManager.Request(Uri.parse(appURL.uri))
                .setTitle(appURL.title)
                .setDescription(appURL.text)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
      val downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
         downloadId.value = downloadID


    }


    @SuppressLint("Range")
     fun checkDownloadStatus(downloadId : Long) : String {
       val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))

        if (cursor != null && cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            cursor.close()

            when(status) {

                DownloadManager.STATUS_FAILED -> {
                    return Const.FAILED
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                 return Const.SUCCESS
                }
                DownloadManager.STATUS_PENDING -> {
                 return  Const.PENDING
                }
                DownloadManager.STATUS_PAUSED -> {
                    return Const.PAUSED
                }
                DownloadManager.ERROR_CANNOT_RESUME -> {
                    return  Const.ERROR_CANNOT_RESUME
                }
            }
        }
        return "Not Found"
    }
}