package com.example.mafia.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mafia.composes.WelcomeCompose
import com.example.mafia.navigation.Navigation
import com.example.mafia.ui.theme.MafiaTheme

class WelcomeActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContent {
            MafiaTheme{
                Navigation(context = this)
            }
        }

    }

}
