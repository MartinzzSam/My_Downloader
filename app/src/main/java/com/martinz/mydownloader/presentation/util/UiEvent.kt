package com.martinz.mydownloader.presentation.util

sealed class UiEvent {

    data class ShowSnackBar(val message : String) : UiEvent()
}
