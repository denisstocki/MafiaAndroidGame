package com.example.mafia.composes

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafia.R
import com.example.mafia.elements.GameStatus
import com.example.mafia.navigation.NavigationRoutes
import com.example.mafia.ui.theme.Black500
import com.example.mafia.ui.theme.Red500
import com.example.mafia.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun DeathCompose(
    navController: NavController,
    gameViewModel: GameViewModel
) {

    val width = LocalConfiguration.current.screenWidthDp.dp           // This variable is used to hold current screen width in dp
    val height = LocalConfiguration.current.screenHeightDp.dp         // This variable is used to hold current screen height in dp

    val time = remember {
        Animatable(0f)
    }

    val drops = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
    )

    drops.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            val delayMillis = (Random.nextFloat() * Random.nextFloat() * 3000L).toLong()
            delay(delayMillis)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 1200 with FastOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        time.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 10000, easing = LinearEasing))
        if(gameViewModel.game.status == GameStatus.AFTER_NIGHT){
            gameViewModel.setGameStatus(GameStatus.DAY_TALK)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black500)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.skull1),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp),
                contentScale = ContentScale.FillBounds
            )
            Box(
                modifier = Modifier
                    .size(width = 300.dp, height = 330.dp),
                contentAlignment = Alignment.Center
            ) {
                val killedPlayer = gameViewModel.latestKilled
                val deadInfoText = if (killedPlayer.value == gameViewModel.game.player!!.nickname) {
                    "YOU ARE"
                } else if(killedPlayer.value == "") {
                        "NOBODY IS"
                    }
                else{
                    "${killedPlayer.value} IS"
                }

                Text(
                    text = deadInfoText,
                    color = Red500,
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.anton_regular)),
                    modifier = Modifier.padding(bottom = 100.dp)
                )
                Text(
                    text = "DEAD",
                    color = Red500,
                    fontSize = 70.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.anton_regular)),
                    modifier = Modifier.padding(top = 150.dp)
                )
            }
        }

        drops.forEachIndexed { index, value ->
            Image(
                painter = painterResource(id = R.drawable.blood_drop2),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .offset(width * (index + 1) / (drops.size + 1), height * value.value),
                contentScale = ContentScale.FillBounds
            )
        }
        Image(
            painter = painterResource(id = R.drawable.blood8),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .offset(0.dp, 0.dp)
                .width(width)
                .height(height / 1.5f),
            contentScale = ContentScale.FillBounds
        )
        Image(
            painter = painterResource(id = R.drawable.frame1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}