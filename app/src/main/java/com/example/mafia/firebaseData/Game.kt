package com.example.mafia.firebaseData

import com.example.mafia.elements.GameStatus
import com.google.firebase.database.Exclude

data class Game (
    @get: Exclude
    var pin: String? = null,
    var player: dbPlayer? = null,
    var status: GameStatus? = null
)