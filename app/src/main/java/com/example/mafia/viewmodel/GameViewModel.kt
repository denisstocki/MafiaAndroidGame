package com.example.mafia.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mafia.elements.LifeStatus
import com.example.mafia.elements.Player
import com.example.mafia.elements.Role
import com.example.mafia.elements.Utility.playerList
import com.example.mafia.firebaseData.Game
import com.example.mafia.firebaseData.dbPlayer
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GameViewModel : ViewModel() {
    var game = Game()
    private val gamesReference = FirebaseDatabase.getInstance().getReference("games")
    private val pinsReference = FirebaseDatabase.getInstance().getReference("GamePinNumbers")

    var playerList = mutableStateListOf<dbPlayer>()

    private var assignFlag = false

    fun asssignListener(){
        val lobbyRef = gamesReference.child(game.pin!!)
        assignFlag = true

        // Rejestrowanie obserwatora dla zdarzenia childAdded
        lobbyRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val playerData = snapshot.value as? Map<*, *>

                playerData?.let {
                    val nickname = it["nickname"] as? String
                    val role = it["role"] as? String
                    val lifeStatus = it["lifeStatus"] as? String
                    val isAdmin = it["isAdmin"] as? Boolean

                    if (nickname != null && role != null && lifeStatus != null && isAdmin != null) {
                        val player = dbPlayer(
                            nickname,
                            Role.valueOf(role),
                            LifeStatus.valueOf(lifeStatus),
                            isAdmin
                        )
                        // Wykonaj odpowiednie akcje na podstawie odczytanych danych gracza
                        playerList.add(player)
                        Log.println(Log.ASSERT,"Test", "${player.nickname}")
                    } else {
                        // Dane gracza są niekompletne
                        Log.println(Log.ASSERT,"Test", "NIEKOMPLETNE")
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val playerData = snapshot.value as? Map<*, *>

                playerData?.let {
                    val nickname = it["nickname"] as? String
                    val role = it["role"] as? String
                    val lifeStatus = it["lifeStatus"] as? String
                    val isAdmin = it["isAdmin"] as? Boolean

                    if (nickname != null && role != null && lifeStatus != null && isAdmin != null) {
                        val playerChanged = dbPlayer(
                            nickname,
                            Role.valueOf(role),
                            LifeStatus.valueOf(lifeStatus),
                            isAdmin
                        )

                        // Wykonaj odpowiednie akcje na podstawie odczytanych danych gracza
                        for(player in playerList){
                            if(player.nickname == playerChanged.nickname){
                                player.isAdmin = playerChanged.isAdmin
                            }
                        }

                        Log.println(Log.ASSERT,"Test", "changed player ${playerChanged.nickname}")
                    } else {
                        // Dane gracza są niekompletne
                        Log.println(Log.ASSERT,"Test", "NIEKOMPLETNE")
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Usunięcie gracza z lobby
                val playerData = snapshot.value as? Map<*, *>

                playerData?.let {
                    val nickname = it["nickname"] as? String
                    val role = it["role"] as? String
                    val lifeStatus = it["lifeStatus"] as? String
                    val isAdmin = it["isAdmin"] as? Boolean

                    if (nickname != null && role != null && lifeStatus != null && isAdmin != null) {
                        val playerToRemove = dbPlayer(
                            nickname,
                            Role.valueOf(role),
                            LifeStatus.valueOf(lifeStatus),
                            isAdmin
                        )

                        // Wykonaj odpowiednie akcje na podstawie odczytanych danych gracza
                        for (player in playerList){
                            if(player.nickname!! == playerToRemove.nickname){
                                playerList.remove(player)
                            }
                        }

                        // Ustaw nowego admina
                        if(playerList.size!=0) {
                            if (playerToRemove.isAdmin!!) {
                                val randomPlayer = playerList[0]
                                randomPlayer.isAdmin = true

                                val updatedPlayerValues = mapOf(
                                    "isAdmin" to randomPlayer.isAdmin
                                )
                                gamesReference.child(game.pin!!).child(randomPlayer.nickname!!).updateChildren(updatedPlayerValues)
                            }
                        }
                        else {
                            gamesReference.child(game.pin!!).removeValue()
                            pinsReference.child(game.pin!!).setValue(true)
                        }

                        Log.println(Log.ASSERT,"Test", "player removed${playerToRemove.nickname}")
                    }
                    else {
                        // Dane gracza są niekompletne
                        Log.println(Log.ASSERT,"Test", "NIEKOMPLETNE")
                    }
                }

            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Przeniesienie gracza w lobby
                val movedPlayer = snapshot.getValue(Player::class.java)
                Log.println(Log.ASSERT,"Test", "MOVED PLAYER")
                // Wykonaj odpowiednie akcje na podstawie przeniesienia gracza
            }

            override fun onCancelled(error: DatabaseError) {
                // Obsługa błędu, jeśli wystąpił
            }
        })
    }

    fun createGame(waiting: MutableState<Boolean>, gamePin: MutableState<String>){
        game = Game()
        playerList.clear()
        assignFlag = false

        val databaseReference = FirebaseDatabase.getInstance().getReference("GamePinNumbers")

        val query = databaseReference.orderByValue().equalTo(true).limitToFirst(1)
        Log.println(Log.ASSERT,"TEST", "Przed listener")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val key = dataSnapshot.children.first().key
                    game.pin = key
                    if (key != null) {
                        gamePin.value = key
                        Log.println(Log.ASSERT,"Test", "JEBAC3")
                    }
                    else{
                        gamePin.value = null.toString()
                    }
                    waiting.value = true

                    val selectedKeyReference = databaseReference.child(key!!)
                    selectedKeyReference.setValue(false)

                } else {
                    println("Brak kluczy o wartości true")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Obsługa błędu, jeśli taka będzie potrzebna
            }
        })
    }

    fun joinToGame(gamePin: String){
        game.pin = gamePin
        playerList = mutableStateListOf<dbPlayer>()

        if(!assignFlag) {
            asssignListener()
        }
    }


    fun createPlayer(nickname: String, isAdmin:Boolean = false) {
        val player = dbPlayer(nickname,Role.EMPTY,LifeStatus.ALIVE,isAdmin)

        val playerValues = mapOf(
            "nickname" to player.nickname.toString(),
            "role" to player.role.toString(),
            "lifeStatus" to player.lifeStatus.toString(),
            "isAdmin" to player.isAdmin
        )

        gamesReference.child(game.pin!!).child(player.nickname!!).updateChildren(playerValues)

        game.player = player

        if(!assignFlag) {
            asssignListener()
        }
    }

    fun removePlayer(){
        Log.println(Log.ASSERT,"Test", "Usuwam ${game.player!!.nickname!!}")
        gamesReference.child(game.pin!!).child(game.player!!.nickname!!).removeValue()
    }

    fun resetPinNumbers(){
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("GamePinNumbers")

        val dataMap = HashMap<String, Boolean>()
        for (i in 1000..9999){
            dataMap[i.toString()] = true
        }
        databaseReference.setValue(dataMap)
    }
}
