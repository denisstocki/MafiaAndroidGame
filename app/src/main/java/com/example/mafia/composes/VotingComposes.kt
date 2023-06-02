package com.example.mafia.composes

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafia.R
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.mafia.elements.arrestedNote
import com.example.mafia.elements.GameStatus
import com.example.mafia.elements.LifeStatus
import com.example.mafia.elements.Player
import com.example.mafia.elements.Role
import com.example.mafia.elements.deathNote
import com.example.mafia.firebaseData.dbPlayer
import com.example.mafia.ui.theme.Red500
import com.example.mafia.viewmodel.GameViewModel

@Composable
fun VotingCompose(
    navController: NavController,
    gameViewModel: GameViewModel
) {
    val playerList = ArrayList<dbPlayer>()

    if(gameViewModel.game.player!!.lifeStatus == LifeStatus.ALIVE) { // VOTING SCREEN ONLY FOR ALIVE PEOPLE
        when (gameViewModel.game.status){

            GameStatus.NIGHT_VOTING -> { // VOTING FOR NIGHT
                when (gameViewModel.game.player!!.role) {
                    Role.MAFIA -> { // ALL EXCEPT MAFIA PLAYERS
                        for (player in gameViewModel.playerList) {
                            if ( player.lifeStatus == LifeStatus.ALIVE && player.role != Role.MAFIA ) {
                                playerList.add(player)
                            }
                        }
                    }
                    Role.DOCTOR -> {
                        for (player in gameViewModel.playerList) {
                            playerList.add(player)
                        }
                    }
                    Role.DETECTIVE -> {
                        for (player in gameViewModel.playerList) {
                            playerList.add(player)
                        }
                    }
                    else -> {}
                }
            }

            GameStatus.DAY_VOTING -> { // VOTING FOR DAY
                for (player in gameViewModel.playerList) {
                    if ( player.lifeStatus == LifeStatus.ALIVE) {
                        playerList.add(player)
                    }
                }
            }

            else -> {}
        }

    }
    else {
        DeathCompose(navController = navController, gameViewModel = gameViewModel)
    }

    val width = LocalConfiguration.current.screenWidthDp.dp           // This variable is used to hold current screen width in dp
    val height = LocalConfiguration.current.screenHeightDp.dp         // This variable is used to hold current screen height in dp

    val used = remember { Animatable(0f) }

    var voted: Boolean by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        used.animateTo(1.0f, animationSpec = tween(durationMillis = 35000, easing = LinearEasing))
        if (gameViewModel.game.status == GameStatus.NIGHT_VOTING) {
            gameViewModel.finishVote(Role.MAFIA)
            gameViewModel.finishVote(Role.DOCTOR)
        }
        else {
            gameViewModel.finishDayVote()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Red500)
    ) {
        Image(                                                            // This image view is used to hold the upper blood image
            painter = painterResource(id = R.drawable.blood8),
            contentDescription = null,
            modifier = Modifier
                .offset(0.dp, 0.dp)
                .fillMaxWidth()
                .size(width)
        )
        Image(                                                            // This image view is used to hold the lower blood image
            painter = painterResource(id = R.drawable.blood9),
            contentDescription = null,
            modifier = Modifier
                .offset(0.dp, height - 430.dp)
                .fillMaxWidth()
                .size(width)
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val voting = when (gameViewModel.game.status) {
                GameStatus.DAY_VOTING -> {
                    "DAY VOTING"
                }
                GameStatus.NIGHT_VOTING -> {
                    "NIGHT VOTING"
                }
                else -> {
                    ""
                }
            }
            Text(
                text = voting,
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .size(width = (width - 40.dp), height = 40.dp)
                    .border(BorderStroke(5.dp, Color.Black), RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = (width - 40.dp) * (1.0f - used.value), height = 40.dp)
                            .border(
                                BorderStroke(5.dp, Color.Black),
                                RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                            )
                            .background(Color.White)
                            .clip(RoundedCornerShape(20.dp))
                    )
                    Box(
                        modifier = Modifier
                            .size(width = (width - 40.dp) * used.value, height = 40.dp)
                            .border(
                                BorderStroke(5.dp, Color.Black),
                                RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                            )
                            .background(Color.Black)
                            .clip(RoundedCornerShape(20.dp))
                    )
                }
            }

            var votedPlayer by remember {
                mutableStateOf(dbPlayer())
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(height - 210.dp)
                .background(Color.Transparent),
                contentAlignment = Alignment.Center) {

                if (!voted) {
                    LazyRow(
                        modifier = Modifier
                            .padding((width - 300.dp) / 2 + 1.dp)
                    ) {
                        items(playerList) { player ->
                            Player("", Role.EMPTY).votePlayer(player){
                                gameViewModel.playerVote(player)
                                votedPlayer = player
                                voted = true
                            }
                        }
                    }

                } else {
                    Box(modifier = Modifier.padding((width - 300.dp) / 2)) {
                        if (gameViewModel.game.status == GameStatus.NIGHT_VOTING) {
                            deathNote(votedPlayer, gameViewModel.game.player!!.role!!)
                        }
                        else {
                            arrestedNote(player = votedPlayer)
                        }
                    }
                }
            }

        }
    }
}
