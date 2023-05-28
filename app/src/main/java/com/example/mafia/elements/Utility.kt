package com.example.mafia.elements

import java.util.concurrent.TimeUnit

object Utility {

    // time to countdown - 1hr - 60secs
    const val TIME_COUNTDOWN: Long = 60000L
    private const val TIME_FORMAT = "%02d:%02d"
    val playerList: ArrayList<Player> = createPlayer()

    // convert time to milli seconds
    fun Long.formatTime(): String = String.format(
        TIME_FORMAT,
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )

    private fun createPlayer (): ArrayList<Player>{

        val playersList = ArrayList<Player>()
        playersList.add(Player("WIKTORIA", Role.MAFIA))
        playersList.add(Player("MARTYNA", Role.DETECTIVE))
        playersList.add(Player("KASIA", Role.MAFIA))
        playersList.add(Player("DAWID", Role.CIVIL))
        playersList.add(Player("DENIS", Role.CIVIL))
        playersList.add(Player("IGOR", Role.DETECTIVE))
        playersList.add(Player("JAKUB", Role.CIVIL))
        playersList.add(Player("KAROL", Role.CIVIL))
        playersList.add(Player("MIKOŁAJ", Role.DOCTOR))
        playersList.add(Player("MIKOŁAJ", Role.DOCTOR))
        playersList.add(Player("MIKOŁAJ", Role.DOCTOR))
        playersList.add(Player("MIKOŁAJ", Role.DOCTOR))
        playersList.add(Player("MIKOŁAJ", Role.DOCTOR))
        playersList.add(Player("MIKOŁAJ", Role.DOCTOR))
        playersList.add(Player("MIKOŁAJ", Role.DOCTOR))
        playersList.add(Player("MIKOŁAJ", Role.DOCTOR))

        return playersList
    }
}