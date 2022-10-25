package com.martinz.mydownloader.presentation.main_activity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.martinz.mydownloader.R
import com.martinz.mydownloader.databinding.ActivityMainBinding
import com.martinz.mydownloader.presentation.ButtonState
import com.martinz.mydownloader.presentation.DownloaderEvent
import com.martinz.mydownloader.presentation.DownloaderState
import com.martinz.mydownloader.presentation.util.AppURL
import com.martinz.mydownloader.presentation.util.DownloaderNotification
import com.martinz.mydownloader.presentation.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notification: DownloaderNotification


    lateinit var downloadState: DownloaderState

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        binding.contentMain.viewModel = mainViewModel
        setContentView(view)

        lifecycleScope.launchWhenCreated {
            launch {stateObserver()  }
            launch {  uiEventCollector() }
        }

        customLinkObserver()




        registerReceiver(mReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        onClicks()
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadState.downloadId == id) {
                mainViewModel.onEvent(DownloaderEvent.UpdateButtonStatus(ButtonState.Completed))
                val status = mainViewModel.checkStatus(downloadId = downloadState.downloadId)
                notification.createNotification(
                    title = downloadState.selection?.title ?: "Unknown",
                    text = downloadState.selection?.text ?: "Unknown",
                    downloadStatus = status.toString()
                )

            }
        }
    }


    private fun onClicks() {
        binding.contentMain.apply {
            rbGroup.setOnCheckedChangeListener { radioGroup, i ->


                if (i != R.id.rb_Other) {
                    etCustom.text.clear()
                    etCustom.visibility = View.GONE
                } else {
                    etCustom.visibility = View.VISIBLE
                }
                val appURL = when (i) {
                    R.id.rb_Glide -> AppURL.GlideLink()
                    R.id.rb_UdacityRepo -> AppURL.UdacityLink()
                    R.id.rb_Retrofit -> AppURL.RetrofitLink()
                    else -> AppURL.CustomLink(customUrl = "")

                }

                mainViewModel.onEvent(DownloaderEvent.UpdateSelection(appURL))


            }


            btnDownload.setOnClickListener {
                if (downloadState.selection != null) {
                    if (downloadState.buttonState !is ButtonState.Loading) {

                        if (downloadState.selection is AppURL.CustomLink) {
                            btnDownload.mButtonState = ButtonState.Clicked
                            mainViewModel.onEvent(DownloaderEvent.Download)
                        } else {
                            btnDownload.mButtonState = ButtonState.Clicked
                             mainViewModel.onEvent(DownloaderEvent.Download)
                        }

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Please Wait Until Download Finish",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Please Select File First",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }
    }


    private suspend fun stateObserver() {
        mainViewModel.downloaderState.collect() {
            downloadState = it
            if (it.buttonState != null) {
                binding.contentMain.btnDownload.mButtonState = it.buttonState
            }
            if (it.buttonState is ButtonState.Loading) {
                binding.contentMain.apply {
                    rbGroup.forEach { view ->
                        view.isEnabled = false
                    }
                }
            } else {
                binding.contentMain.rbGroup.forEach { view ->
                    delay(500)
                    view.isEnabled = true
                }
            }

        }
    }

    private suspend fun uiEventCollector() {
        mainViewModel.uiEvent.collect { uiEvent ->

            when(uiEvent) {

                is UiEvent.ShowSnackBar -> {
                  showSnackBarMessage(uiEvent.message)
                }
            }

        }
    }


    private fun customLinkObserver() {
        mainViewModel.customLink.observe(this) {
            mainViewModel.onEvent(DownloaderEvent.UpdateSelection(AppURL.CustomLink(it)))
        }
    }


    private fun showSnackBarMessage(message: String) {
        val snack = Snackbar.make(this.findViewById(android.R.id.content), message, 1500)
        snack.show()
    }
}