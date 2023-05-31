package com.example.mafia.firebaseData

import com.example.mafia.elements.Role
import com.google.firebase.database.Exclude

data class Vote (
    @get:Exclude
    var voteFromPlayer: String? = null,
    var playerVoted: String? = null,
    var voteRole: Role? = null,
)