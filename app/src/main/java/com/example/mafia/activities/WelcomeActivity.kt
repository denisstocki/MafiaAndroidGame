package com.example.mafia.activities

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import com.example.mafia.composes.ChangeNavigationBarColor
import com.example.mafia.composes.WelcomeCompose
import com.example.mafia.elements.Utility
import com.example.mafia.navigation.Navigation
import com.example.mafia.ui.theme.Black200
import com.example.mafia.ui.theme.MafiaTheme
import com.example.mafia.viewmodel.GameViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.database.FirebaseDatabase

class WelcomeActivity : ComponentActivity() {

    private val gameViewModel = GameViewModel()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {

            ChangeNavigationBarColor(color = Black200)

            MafiaTheme{
                Navigation(context = this,gameViewModel)
            }
        }
    }
}
