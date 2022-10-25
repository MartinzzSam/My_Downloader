package com.martinz.mydownloader.presentation

import com.martinz.mydownloader.presentation.util.AppURL

sealed class DownloaderEvent {
    object Download : DownloaderEvent()
    data class UpdateDownloadId(val downloadId : Long) : DownloaderEvent()
    data class UpdateButtonStatus(val buttonState: ButtonState) : DownloaderEvent()
    data class UpdateDownloaderStatus(val downloadStatus: String) : DownloaderEvent()
    data class UpdateSelection(val updateSelection: AppURL) : DownloaderEvent()
}
