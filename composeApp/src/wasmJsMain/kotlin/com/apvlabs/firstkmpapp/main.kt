package com.apvlabs.firstkmpapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport("root") {
        WorldClockTheme {
            App()
        }
    }
}
