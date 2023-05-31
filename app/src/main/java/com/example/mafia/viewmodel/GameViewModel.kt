package com.example.mafia.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mafia.elements.LifeStatus
import com.example.mafia.elements.Player
import com.example.mafia.elements.Role
import com.example.mafia.firebaseData.Game
import com.example.mafia.firebaseData.dbPlayer
import com.example.mafia.navigation.NavigationRoutes
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CompletableFuture

class GameViewModel : ViewModel() {
    // TODO: sprawdzenie nickname i pinu w joinie
    // TODO: przydzielenie roli po nacisnieciu start w lobby
    // TODO: odpowiednie czasy trwania ustawic
    var game = Game()
    private val gamesReference = FirebaseDatabase.getInstance().getReference("games")
    private val pinsReference = FirebaseDatabase.getInstance().getReference("GamePinNumbers")

    var playerList = mutableStateListOf<dbPlayer>()
    var ifIamAdmin: MutableState<Boolean> = mutableStateOf(false)

    private var assignFlag = false

    private lateinit var gameStatusListener: ValueEventListener


    fun assignListener(){
        val lobbyRef = gamesReference.child(game.pin!!)
        assignFlag = true
        Log.println(Log.ASSERT,"Test", "Listener on ${game.pin}")

        // Rejestrowanie obserwatora dla zdarzenia childAdded
        lobbyRef.addChildEventListener(childEventListenerOnGame)
    }

    fun assignListenerForGameStatus(navController: NavController){
        val gameStatusReference = gamesReference.child(game.pin!!).child("game_status")

        gameStatusListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                when(snapshot.value){
                    "Started" -> {
                        navController.navigate(NavigationRoutes.Loading.route)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        gameStatusReference.addValueEventListener(gameStatusListener)
    }

    fun createGame(waiting: MutableState<Boolean>, gamePin: MutableState<String>){
        game = Game()
        playerList.clear()
        assignFlag = false
        ifIamAdmin.value = true

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
        playerList.clear()

        if(!assignFlag) {
            assignListener()
        }
    }

    fun unReserveGame(pin: String) {
        val pin = pinsReference.child(pin)

        pin.setValue(true)
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
            assignListener()
        }
    }

    fun removePlayer(){
        Log.println(Log.ASSERT,"Test", "Usuwam ${game.player!!.nickname!!}")
        gamesReference.child(game.pin!!).child(game.player!!.nickname!!).removeValue()
        gamesReference.child(game.pin!!).removeEventListener(childEventListenerOnGame)
        gamesReference.child(game.pin!!).child("game_status").removeEventListener(gameStatusListener)

        if(playerList.size == 1){
            gamesReference.child(game.pin!!).removeValue()
            pinsReference.child(game.pin!!).setValue(true)
        }
        assignFlag = false
        ifIamAdmin.value = false
    }

    fun startGameForAll(){
        gamesReference.child(game.pin!!).child("game_status").setValue("Started")
    }
    fun assignRoles(){
        game.player!!.role = Role.random()
        var mafiaAmount = if(playerList.size/3 < 1) 1 else playerList.size/3
        var medicAmount = if(playerList.size/6 < 1) 1 else playerList.size/6
        var detectiveAmount = if(playerList.size/4 < 1) 1 else playerList.size/4
        var civilAmount = playerList.size - mafiaAmount - medicAmount - detectiveAmount

        for(player in playerList){
            var rolePicked = false
            while(!rolePicked){
                when(Role.random()){
                    Role.MAFIA -> {
                        if(mafiaAmount > 0){
                            player.role = Role.MAFIA
                            mafiaAmount--
                            rolePicked = true

                        }
                    }
                    Role.DETECTIVE -> {
                        if (detectiveAmount > 0) {
                            player.role = Role.DETECTIVE
                            detectiveAmount--
                            rolePicked = true
                        }
                    }
                    Role.CIVIL -> {
                        if(civilAmount > 0){
                            player.role = Role.CIVIL
                            civilAmount--
                            rolePicked = true
                        }
                    }
                    Role.DOCTOR -> {
                        if(medicAmount > 0){
                            player.role = Role.DOCTOR
                            medicAmount--
                            rolePicked = true
                        }
                    }
                    Role.EMPTY -> {}
                }
                gamesReference.child(game.pin!!).child(player.nickname!!).updateChildren(mapOf("role" to player.role.toString()))

                if(player.nickname == game.player!!.nickname){
                    game.player!!.role = player.role
                }
            }
        }

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

    fun gameIncludesNickname(pin: String, nickname: String): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()

        gamesReference.child(pin).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isIncluded = false
                Log.i("ELOELOELO", "przed callback")
                Log.i("ELOELOELO", "a to wynik: $isIncluded")
                dataSnapshot.children.forEach { childSnap ->
                    Log.i("ELOELOELO", "przechodze ${childSnap}")
                    Log.i("ELOELOELO", "przechodze ${childSnap.key}")
                    Log.i("ELOELOELO", "przechodze ${pin}")

                    if (childSnap.key.toString() == nickname) {
                        Log.i("ELOELOELO", "znalazlem")

                        isIncluded = true
                    }
                }

                completableFuture.complete(isIncluded)
                Log.i("ELOELOELO", "po callback")

            }

            override fun onCancelled(databaseError: DatabaseError) {
                completableFuture.completeExceptionally(databaseError.toException())
            }
        })
        Log.i("ELOELOELO", "nickname finished")

        return completableFuture
    }

    fun gameIsStarted(pin: String): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()
        Log.i("ELOELOELO", "przechodze ${pin}")


        gamesReference.child(pin).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isStarted = false
                Log.i("ELOELOELO", "przed callback empty")
                Log.i("ELOELOELO", "snapiiiii ${dataSnapshot}")
                val dataSnapshotGameStatus = dataSnapshot.child("game_status")
                val gameStatusValue = dataSnapshotGameStatus.getValue(String::class.java)

                if (gameStatusValue == "Started") {
                    isStarted = true
                }
                Log.i("ELOELOELO", "a to wynik empty: $isStarted")

                completableFuture.complete(isStarted)
                Log.i("ELOELOELO", "po callback empty")

            }

            override fun onCancelled(databaseError: DatabaseError) {
                completableFuture.completeExceptionally(databaseError.toException())
            }
        })
        Log.i("ELOELOELO", "empty finished")

        return completableFuture
    }

    fun gameIsEmpty(pin: String): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()
        Log.i("ELOELOELO", "przechodze ${pin}")


        gamesReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var isEmpty = true
                Log.i("ELOELOELO", "przed callback empty")
                dataSnapshot.children.forEach { childSnap ->
                    Log.i("ELOELOELO", "przechodze ${childSnap}")
                    Log.i("ELOELOELO", "przechodze ${childSnap.key}")
                    Log.i("ELOELOELO", "przechodze ${pin}")

                    if (childSnap.key.toString() == pin) {
                        Log.i("ELOELOELO", "znalazlem")

                        isEmpty = false
                    }
                }
                Log.i("ELOELOELO", "a to wynik empty: $isEmpty")

                completableFuture.complete(isEmpty)
                Log.i("ELOELOELO", "po callback empty")

            }

            override fun onCancelled(databaseError: DatabaseError) {
                completableFuture.completeExceptionally(databaseError.toException())
            }
        })
        Log.i("ELOELOELO", "empty finished")

        return completableFuture
    }


    private val childEventListenerOnGame = object : ChildEventListener {
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
                    Log.println(Log.ASSERT,"Test", "added ${player.nickname}")
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
                            player.role = playerChanged.role
                            player.lifeStatus = playerChanged.lifeStatus

                            ifIamAdmin.value = playerChanged.isAdmin!!
                        }
                    }

                    if(playerChanged.nickname == game.player!!.nickname){
                        game.player!!.role = playerChanged.role
                    }

                    Log.println(Log.ASSERT,"Test", "changed player ${playerChanged.nickname} ${playerChanged.isAdmin}")
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
    }
}
