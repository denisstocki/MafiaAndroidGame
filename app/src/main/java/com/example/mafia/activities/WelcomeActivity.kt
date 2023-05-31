package com.example.mafia.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mafia.composes.WelcomeCompose
import com.example.mafia.navigation.Navigation
import com.example.mafia.ui.theme.MafiaTheme
import com.example.mafia.viewmodel.GameViewModel
import com.google.firebase.database.FirebaseDatabase

class WelcomeActivity : ComponentActivity() {

    private val gameViewModel = GameViewModel()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {
            MafiaTheme{
                Navigation(context = this,gameViewModel)
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        Log.i("ELOELOELO", "DESTROOOOOOOOOOOOOOOOOOU")
//        gameViewModel.removePlayer()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.i("ELOELOELO", "STOOOOOOOOOOOOOOOOOOOP")
//        gameViewModel.removePlayer()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.i("ELOELOELO", "PAUUUUUUUUUUUUUUSE")
//        gameViewModel.removePlayer()
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//    }
}
