package com.example.mafia.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.mafia.elements.LifeStatus
import com.example.mafia.elements.Player
import com.example.mafia.elements.Role
import com.example.mafia.firebaseData.Game
import com.example.mafia.firebaseData.dbPlayer
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameViewModel : ViewModel() {
    var game = Game()
    val dbGames = FirebaseDatabase.getInstance().getReference("games")


    fun pushGame(){
        val lobbyRef = dbGames.child(game.pin!!)
        Log.println(Log.ASSERT,"Test", "JEBAC5")

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
                        game.playerList.add(player)
                    } else {
                        // Dane gracza są niekompletne
                        Log.println(Log.ASSERT,"Test", "NIEKOMPLETNE")
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Zmiana danych gracza w lobby
                val updatedPlayer = snapshot.getValue(Player::class.java)
                // Wykonaj odpowiednie akcje na podstawie zaktualizowanych danych gracza
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Usunięcie gracza z lobby
                val removedPlayer = snapshot.getValue(Player::class.java)
                // Wykonaj odpowiednie akcje na podstawie usunięcia gracza
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Przeniesienie gracza w lobby
                val movedPlayer = snapshot.getValue(Player::class.java)
                // Wykonaj odpowiednie akcje na podstawie przeniesienia gracza
            }

            override fun onCancelled(error: DatabaseError) {
                // Obsługa błędu, jeśli wystąpił
            }
        })
    }

    fun createGame(waiting: MutableState<Boolean>, gamePin: MutableState<String>){
        game = Game()
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


    fun createPlayer(nickname: String, isAdmin:Boolean = false) {
        val player = dbPlayer(nickname,Role.EMPTY,LifeStatus.ALIVE,isAdmin)
        val playerValues = mapOf(
            "nickname" to player.nickname.toString(),
            "role" to player.role.toString(),
            "lifeStatus" to player.lifeStatus.toString(),
            "isAdmin" to player.isAdmin
        )

        dbGames.child(game.pin!!).child(player.nickname!!).updateChildren(playerValues)
    }
}