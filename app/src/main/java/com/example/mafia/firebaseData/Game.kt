package com.example.mafia.firebaseData

import com.google.firebase.database.Exclude

data class Game (
    @get: Exclude
    var pin: String? = null,
    var player: dbPlayer? = null
)