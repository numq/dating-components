package application

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.singleWindowApplication
import navigation.Navigation

fun main() = singleWindowApplication {
    MaterialTheme {
        Navigation()
    }
}