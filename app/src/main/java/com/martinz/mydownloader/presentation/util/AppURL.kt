package com.martinz.mydownloader.presentation.util

import com.martinz.mydownloader.Const.GLIDE_TEXT
import com.martinz.mydownloader.Const.GLIDE_TITLE
import com.martinz.mydownloader.Const.GLIDE_URL
import com.martinz.mydownloader.Const.RETROFIT_TEXT
import com.martinz.mydownloader.Const.RETROFIT_TITLE
import com.martinz.mydownloader.Const.RETROFIT_URL
import com.martinz.mydownloader.Const.UDACITY_TEXT
import com.martinz.mydownloader.Const.UDACITY_TITLE
import com.martinz.mydownloader.Const.UDACITY_URL

sealed class AppURL(val uri: String, val title: String, val text: String){

    data class GlideLink(
        val glideUrl: String = GLIDE_URL,
        val glideTitle: String = GLIDE_TITLE,
        val glideText: String = GLIDE_TEXT
        ) : AppURL(glideUrl, glideTitle, glideText)

    data class UdacityLink(
        val udacityUrl: String = UDACITY_URL,
        val udacityTitle: String = UDACITY_TITLE,
        val udacityText: String = UDACITY_TEXT
    ) : AppURL(udacityUrl, udacityTitle, udacityText)


    data class RetrofitLink(
        val retrofitUrl: String = RETROFIT_URL,
        val retrofitTitle: String = RETROFIT_TITLE,
        val retrofitText: String = RETROFIT_TEXT
    ) : AppURL(retrofitUrl, retrofitTitle, retrofitText)



    data class CustomLink(
        val customUrl: String,
        val customTitle: String = "Custom Download",
        val customText: String = "Custom Download Repository"
    ) : AppURL(customUrl, customTitle, customText)



}