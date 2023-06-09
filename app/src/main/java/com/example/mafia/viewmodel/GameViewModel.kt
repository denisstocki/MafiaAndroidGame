package com.example.mafia.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mafia.elements.GameStatus
import com.example.mafia.elements.LifeStatus
import com.example.mafia.elements.Role
import com.example.mafia.firebaseData.Game
import com.example.mafia.firebaseData.Vote
import com.example.mafia.firebaseData.dbPlayer
import com.example.mafia.navigation.NavigationRoutes
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.CompletableFuture

class GameViewModel : ViewModel() {
    // TODO: odpowiednie czasy trwania ustawic
    var game = Game()
    private val gamesReference = FirebaseDatabase.getInstance().getReference("games")
    private val pinsReference = FirebaseDatabase.getInstance().getReference("GamePinNumbers")
    private lateinit var gameStatusReference: DatabaseReference
    private lateinit var voteReference: DatabaseReference

    var playerList = mutableStateListOf<dbPlayer>()
    var voteList = mutableStateListOf<Vote>()
    var ifIamAdmin: MutableState<Boolean> = mutableStateOf(false)
    var latestKilled: MutableState<String> = mutableStateOf("")

    private var assignFlag = false
    private var votingAssignFlag = false

    private lateinit var gameStatusListener: ValueEventListener


    private fun assignListener(){
        val lobbyRef = gamesReference.child(game.pin!!)
        assignFlag = true
        Log.println(Log.ASSERT,"Test", "Listener on ${game.pin}")

        // Rejestrowanie obserwatora dla zdarzenia childAdded
        lobbyRef.addChildEventListener(childEventListenerOnGame)
    }



    /** -------------------- GAME-STATUS----------------------*/
    fun startGameForAll(){
        gameStatusReference.setValue(GameStatus.STARTED)
    }

    fun setGameStatus (status: GameStatus){
        gameStatusReference.setValue(status)
    }

    fun assignListenerForGameStatus(navController: NavController){
        gameStatusReference = gamesReference.child(game.pin!!).child("game_status")
        voteReference = gamesReference.child(game.pin!!).child("voting").child(game.player!!.nickname!!)

        gameStatusListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                when(snapshot.value){

                    GameStatus.STARTED.toString() -> { // STARTED
                        game.status = GameStatus.STARTED
                        navController.navigate(NavigationRoutes.Loading.route)
                    }

                    GameStatus.NIGHT_VOTING.toString() -> { // NIGHT VOTING
                        game.status = checkForWin (GameStatus.NIGHT_VOTING)
                        Log.println(Log.ASSERT,"TEST", "NIGHT_VOTING")

                        when(game.status) {
                            GameStatus.MAFIA_WIN -> navController.navigate(NavigationRoutes.Win.route)
                            GameStatus.TOWN_WIN -> navController.navigate(NavigationRoutes.Win.route)
                            else -> navController.navigate(NavigationRoutes.Voting.route)
                        }
                    }

                    GameStatus.AFTER_NIGHT.toString() -> { // WHO IS DEAD OR NOT
                        game.status = GameStatus.AFTER_NIGHT
                        Log.println(Log.ASSERT,"TEST", "AFTER_NIGHT")
                        navController.navigate(NavigationRoutes.Death.route)
                    }

                    GameStatus.DAY_TALK.toString() -> { // TIME TO TALK
                        game.status = checkForWin (GameStatus.DAY_TALK)
                        Log.println(Log.ASSERT,"TEST", "DAY_TALK")

                        when(game.status) {
                            GameStatus.MAFIA_WIN -> navController.navigate(NavigationRoutes.Win.route)
                            GameStatus.TOWN_WIN -> navController.navigate(NavigationRoutes.Win.route)
                            else -> navController.navigate(NavigationRoutes.Day.route)
                        }
                    }

                    GameStatus.DAY_VOTING.toString() -> { // ALL PLAYERS ARE VOTING
                        Log.println(Log.ASSERT,"TEST", "DAY_VOTING")
                        game.status = GameStatus.DAY_VOTING
                        navController.navigate(NavigationRoutes.Voting.route)
                    }

                    GameStatus.AFTER_DAY.toString() -> { // ARRESTING POTENTIAL MAFIA PLAYER
                        game.status = GameStatus.AFTER_DAY
                        Log.println(Log.ASSERT,"TEST", "AFTER_DAY")
                        navController.navigate(NavigationRoutes.Arrest.route)
                    }

                    GameStatus.MAFIA_WIN.toString() -> { // ARRESTING POTENTIAL MAFIA PLAYER
                        game.status = GameStatus.MAFIA_WIN
                        navController.navigate(NavigationRoutes.Win.route)
                    }

                    GameStatus.TOWN_WIN.toString() -> { // ARRESTING POTENTIAL MAFIA PLAYER
                        game.status = GameStatus.TOWN_WIN
                        navController.navigate(NavigationRoutes.Win.route)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        gameStatusReference.addValueEventListener(gameStatusListener)
    }

    fun checkForWin (statusIfNotWin: GameStatus): GameStatus {
        var mafiaCount = 0
        var townCount = 0
        for(player in playerList){
            if(player.lifeStatus == LifeStatus.ALIVE){
                if(player.role == Role.MAFIA){
                    mafiaCount++
                }
                else {
                    townCount++
                }
            }
        }
        if (mafiaCount == 0){
            return GameStatus.TOWN_WIN
        }
        else if (mafiaCount == townCount){
            return GameStatus.MAFIA_WIN
        }
        else {
            return statusIfNotWin
        }
    }

    /** -------------------- GAME-STATUS----------------------*/
    /** ------------------------ END -------------------------*/

    fun createGame(waiting: MutableState<Boolean>, gamePin: MutableState<String>){
        game = Game()
        playerList.clear()
        assignFlag = false
        votingAssignFlag = false
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




    /** ---------------------------- VOTING------------------------------------  */
    fun beginVoting() {
        voteList.clear()
        voteReference.removeValue()

        if(!votingAssignFlag) {
            assignVotingListener()
        }

        if(game.status == GameStatus.DAY_TALK){
            setGameStatus(GameStatus.DAY_VOTING)
        }
        else if (game.status == GameStatus.AFTER_DAY){
            setGameStatus(GameStatus.NIGHT_VOTING)
        }
    }

    fun playerVote(playerToVote: dbPlayer){
        val voteValues = mapOf(
            "voteFromPlayer" to game.player!!.nickname.toString(),
            "playerVoted" to playerToVote.nickname.toString(),
            "voteRole" to game.player!!.role.toString(),
        )

        voteReference.updateChildren(voteValues)
    }

    fun finishVote(role: Role) {
        var voteCounter = 0
        var maxCount = 0
        var votedNickname = ""

        for(vote in voteList){
            if(vote.voteRole!! == role){
                voteCounter++

                if(voteCounter > maxCount){
                    maxCount = voteCounter
                    votedNickname = vote.playerVoted!!
                }
            }
        }

        for (player in playerList){
            if(player.nickname == votedNickname){
                if(role == Role.DOCTOR){
                    player.lifeStatus = LifeStatus.ALIVE
                }
                else{
                    player.lifeStatus = LifeStatus.DEAD
                }
                gamesReference.child(game.pin!!).child(player.nickname!!).updateChildren(mapOf("lifeStatus" to player.lifeStatus.toString()))
            }
        }

        if(role == Role.DOCTOR){
            if(game.status == GameStatus.NIGHT_VOTING){
                setGameStatus(GameStatus.AFTER_NIGHT)
            }
            else if (game.status == GameStatus.DAY_VOTING){
                setGameStatus(GameStatus.AFTER_DAY)
            }
        }
    }
    fun finishDayVote(){
        var voteCounter = 0
        var maxCount = 0
        var votedNickname = ""

        for(vote in voteList){
            voteCounter++

            if(voteCounter > maxCount){
                maxCount = voteCounter
                votedNickname = vote.playerVoted!!
            }
        }

        for (player in playerList){
            if(player.nickname == votedNickname){
                player.lifeStatus = LifeStatus.DEAD
                gamesReference.child(game.pin!!).child(player.nickname!!).updateChildren(mapOf("lifeStatus" to player.lifeStatus.toString()))
            }
        }
    }

    private fun assignVotingListener(){
        votingAssignFlag = true
        val votingRef = gamesReference.child(game.pin!!).child("voting")
        Log.println(Log.ASSERT,"Test", "voting listener on ${game.pin}")

        // Rejestrowanie obserwatora dla zdarzenia childAdded
        votingRef.addChildEventListener(votingChildEventListener)
    }

    private val votingChildEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val voteData = snapshot.value as? Map<*, *>

            voteData?.let { it ->// vote added
                val voteFromPlayer = it["voteFromPlayer"] as? String
                val playerVoted = it["playerVoted"] as? String
                val voteRole = it["voteRole"] as? String

                Log.println(Log.ASSERT, "Test", "vote $playerVoted $voteRole")

                if(playerVoted != null && voteRole != null){
                    val vote =  Vote(voteFromPlayer,playerVoted, Role.valueOf(voteRole))

                    voteList.add(vote)
                }
            }

        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            voteList.clear()
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }
    /** ---------------------------- VOTING ------------------------------------  */
    /**------------------------------ END---------------------------------------- */





    /**----------------------- PLAYER LIST HANDLING------------------------ */
    fun createPlayer(nickname: String, isAdmin:Boolean = false) {
        val player = dbPlayer(nickname,Role.EMPTY,LifeStatus.ALIVE,isAdmin)

        val playerValues = mapOf(
            "nickname" to player.nickname.toString(),
            "role" to player.role.toString(),
            "lifeStatus" to player.lifeStatus.toString(),
            "isAdmin" to player.isAdmin,
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
//        gamesReference.child(game.pin!!).child("game_status").removeEventListener(gameStatusListener)


        if(playerList.size == 1){
            gamesReference.child(game.pin!!).removeValue()
            pinsReference.child(game.pin!!).setValue(true)
        }
        assignFlag = false
        ifIamAdmin.value = false
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


    private val childEventListenerOnGame = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val playerData = snapshot.value as? Map<*, *>

            playerData?.let { it ->// player added
                val nickname = it["nickname"] as? String
                val role = it["role"] as? String
                val lifeStatus = it["lifeStatus"] as? String
                val isAdmin = it["isAdmin"] as? Boolean


                if (nickname != null && role != null && lifeStatus != null && isAdmin != null ) {
                    val player = dbPlayer(
                        nickname,
                        Role.valueOf(role),
                        LifeStatus.valueOf(lifeStatus),
                        isAdmin
                    )

                    playerList.add(player)
                    Log.println(Log.ASSERT,"Test", "added ${player.nickname}")

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
                        game.player!!.lifeStatus = playerChanged.lifeStatus
                    }
                    if(playerChanged.lifeStatus == LifeStatus.DEAD){
                        latestKilled.value = playerChanged.nickname!!
                    }
                    else if(playerChanged.lifeStatus == LifeStatus.ALIVE){
                        latestKilled.value = ""
                    }

                    Log.println(Log.ASSERT,"Test", "changed player ${playerChanged.nickname} ${playerChanged.isAdmin}")
                } else {
                    // Dane gracza są niekompletne
                    Log.println(Log.ASSERT,"Test", "NIEKOMPLETNE CHANGED")
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
                    Log.println(Log.ASSERT,"Test", "NIEKOMPLETNE REMOVED")
                }
            }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onCancelled(error: DatabaseError) {
            // Obsługa błędu, jeśli wystąpił
        }
    }
    /**----------------------- PLAYER LIST HANDLING------------------------ */
    /**------------------------------ END -------------------------------- */



    /**--------------------------- JOINING LOBBY ERROR CATCHES------------------------------ */
    fun unReserveGame(pin: String) {
        val pin = pinsReference.child(pin)

        pin.setValue(true)
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

    /**--------------------------- JOINING LOBBY ERROR CATCHES------------------------------ */
    /**------------------------------------- END------------------------------------------- */


    fun deleteGame(){
        gamesReference.child(game.pin!!).child(game.player!!.nickname!!).removeValue()
        gamesReference.child(game.pin!!).removeEventListener(childEventListenerOnGame)
        gamesReference.child(game.pin!!).removeValue()
        pinsReference.child(game.pin!!).setValue(true)

        assignFlag = false
        ifIamAdmin.value = false
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
