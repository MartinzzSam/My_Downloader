package com.martinz.mydownloader.presentation

import com.martinz.mydownloader.presentation.util.AppURL

data class DownloaderState(
    val downloadId : Long = 0,
    val downloaderState : String = "",
    val buttonState : ButtonState? = null,
    val selection : AppURL? = null
)
