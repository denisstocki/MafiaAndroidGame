package com.example.mafia.composes

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafia.R
import com.example.mafia.elements.Player
import com.example.mafia.elements.Utility
import com.example.mafia.elements.deathNote
import com.example.mafia.ui.theme.Black200
import com.example.mafia.ui.theme.Black500
import com.example.mafia.ui.theme.Grey200
import com.example.mafia.ui.theme.Red500
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun WinCompose(
    navController: NavController
) {
    TownWinCompose(navController = navController)
}

@Composable
fun TownWinCompose(
    navController: NavController
) {

    val width = LocalConfiguration.current.screenWidthDp.dp           // This variable is used to hold current screen width in dp
    val height = LocalConfiguration.current.screenHeightDp.dp         // This variable is used to hold current screen height in dp

    val drops = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    drops.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            val delayMillis = (Random.nextFloat() * Random.nextFloat() * 5000L).toLong()
            delay(delayMillis)
            animatable.animateTo(
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 3200
                        0.0f at 0 with LinearEasing
                        1.1f at 3200 with LinearEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black500)
    ) {
        drops.forEachIndexed { index, value ->
            Image(
                painter = painterResource(id = R.drawable.ballons1),
                contentDescription = null,
                modifier = Modifier
                    .width(30.dp)
                    .height(60.dp)
                    .offset(width * (index + 1) / (drops.size + 1), height * (1 - value.value)),
                contentScale = ContentScale.FillBounds
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height - 353.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TOWN WON !",
                color = Red500,
                fontSize = 50.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                modifier = Modifier
                    .padding(top = 16.dp)
            )

            val playerList: ArrayList<Player> by remember {
                mutableStateOf(Utility.playerList)
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(height - 210.dp)
                .background(Color.Transparent),
                contentAlignment = Alignment.Center) {

                LazyRow(
                    modifier = Modifier
                        .padding((width - 300.dp) / 2 + 1.dp)
                ) {
                    items(playerList) { player ->
                        player.votePlayer()
                    }
                }
            }

            var joinGame by remember {
                mutableStateOf(false) // Initially dialog is closed
            }
        }
    }
    Image(
        painter = painterResource(id = R.drawable.town1),
        contentDescription = null,
        modifier = androidx.compose.ui.Modifier
            .size(width)
            .offset(y = height - 353.dp)
    )
}