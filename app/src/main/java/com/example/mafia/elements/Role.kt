package com.example.mafia.elements

import androidx.compose.ui.graphics.Color
import com.example.mafia.R
import com.example.mafia.ui.theme.Grey200
import com.example.mafia.ui.theme.Red500

enum class Role(val mainThemeColor: Color, val backGroundColor: Color, val whoYouAre: Int, val description: Int, val logoImage: Int) {
    MAFIA(Red500, Grey200, R.string.mafia, R.string.mafia_desc, R.drawable.mafia1),

    DETECTIVE(
        Red500, Grey200,
        R.string.detective,
        R.string.detective_desc,
        R.drawable.mafia1),

    CIVIL(Red500, Grey200, R.string.civil, R.string.civil_desc, R.drawable.mafia1),

    DOCTOR(Red500, Grey200, R.string.medic, R.string.medic_desc, R.drawable.mafia1),
    EMPTY(Red500, Grey200,R.string.empty , R.string.medic_desc, R.drawable.mafia1);

    companion object {
        private val values = values()
        fun random(): Role {
            var role: Role
            do{
                role = values.random()
            }
            while(role == EMPTY)
            return role
        }
    }
}