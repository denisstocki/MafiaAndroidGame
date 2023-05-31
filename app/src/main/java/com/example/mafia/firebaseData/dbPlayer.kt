package com.example.mafia.firebaseData

import com.example.mafia.elements.LifeStatus
import com.example.mafia.elements.Role
import com.google.firebase.database.Exclude


data class dbPlayer(
    @get:Exclude
    var nickname: String? = null,
    var role: Role? = null,
    var lifeStatus: LifeStatus? = LifeStatus.ALIVE,
    var isAdmin: Boolean? = false,
    var voteCounter: Int = 0
)
