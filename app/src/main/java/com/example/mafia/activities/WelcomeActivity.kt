package com.example.mafia.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mafia.composes.WelcomeCompose
import com.example.mafia.navigation.Navigation
import com.example.mafia.ui.theme.MafiaTheme
import com.example.mafia.viewmodel.GameViewModel
import com.google.firebase.database.FirebaseDatabase

class WelcomeActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)
        val gameViewModel = GameViewModel()
        gameViewModel.resetPinNumbers()

        setContent {
            MafiaTheme{
                Navigation(context = this,gameViewModel)
            }
        }

    }

}
