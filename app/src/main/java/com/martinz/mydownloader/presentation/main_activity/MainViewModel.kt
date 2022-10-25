package com.martinz.mydownloader.presentation.main_activity

import android.app.NotificationManager
import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martinz.mydownloader.Const
import com.martinz.mydownloader.presentation.*
import com.martinz.mydownloader.presentation.util.AppURL
import com.martinz.mydownloader.presentation.util.DownloaderNotification
import com.martinz.mydownloader.presentation.util.MyDownloader
import com.martinz.mydownloader.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val downloader: MyDownloader,
    private val notification: DownloaderNotification
) : ViewModel() {

    private val _downloaderState : MutableStateFlow<DownloaderState> = MutableStateFlow(DownloaderState())
    val downloaderState : StateFlow<DownloaderState> = _downloaderState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val customLink = MutableLiveData<String>()




    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        viewModelScope.launch {
            _uiEvent.send(UiEvent.ShowSnackBar(throwable.message.toString()))
        }

    }



    init {
        viewModelScope.launch {
            updatableDownloadId()
        }

    }




    fun onEvent(event : DownloaderEvent) {
        when(event) {

            is DownloaderEvent.UpdateSelection -> {
                _downloaderState.value = downloaderState.value.copy(
                    selection = event.updateSelection
                )

            }


            is DownloaderEvent.UpdateButtonStatus -> {
                _downloaderState.value = downloaderState.value.copy(
                    buttonState = event.buttonState
                )
            }


            is DownloaderEvent.UpdateDownloadId -> {
                _downloaderState.value = downloaderState.value.copy(
                    downloadId = event.downloadId
                )
            }


            is DownloaderEvent.UpdateDownloaderStatus -> {
                _downloaderState.value = downloaderState.value.copy(
                    downloaderState = event.downloadStatus
                )
            }


            is DownloaderEvent.Download -> {
                    viewModelScope.launch(Dispatchers.IO + handler) {
                        if (downloaderState.value.buttonState !is ButtonState.Loading) {
                            if (URLUtil.isValidUrl(downloaderState.value.selection?.uri)) {
                                _downloaderState.value = downloaderState.value.copy(
                                    buttonState = ButtonState.Loading
                                )
                                downloader.download(downloaderState.value.selection!!)
                                notification.createNotification(
                                    title = downloaderState.value.selection?.title ?: "Unknown",
                                    text = downloaderState.value.selection?.text ?: "Unknown",
                                    downloadStatus = Const.PENDING
                                )
                            } else {
                                _uiEvent.send(UiEvent.ShowSnackBar("Enter Valid Url"))
                            }


                    }


                }

            }

        }
    }




    private suspend fun updatableDownloadId() {
        downloader.downloadId.collect {
            _downloaderState.value =
                downloaderState.value.copy(
                    downloadId = it
                )

        }
    }




    fun checkStatus(downloadId : Long) = downloader.checkDownloadStatus(downloadId)






}