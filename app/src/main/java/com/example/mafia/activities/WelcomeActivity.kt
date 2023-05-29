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
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("GamePinNumbers")

        val dataMap = HashMap<String, Boolean>()
        for (i in 1000..9999){
            dataMap[i.toString()] = true
        }
        databaseReference.setValue(dataMap)

        val gameViewModel = GameViewModel()

        setContent {
            MafiaTheme{
                Navigation(context = this,gameViewModel)
            }
        }

    }

}
