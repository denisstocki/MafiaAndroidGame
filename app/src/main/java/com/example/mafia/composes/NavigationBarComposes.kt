package com.example.mafia.composes

import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.example.mafia.ui.theme.Black200

@Composable
fun ChangeNavigationBarColor(color: Color) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    activity?.let {

        val flags = it.window.decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        it.window.decorView.systemUiVisibility = flags
        it.window.navigationBarColor = color.toArgb()
    }
}