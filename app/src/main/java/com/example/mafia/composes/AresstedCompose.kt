package com.example.mafia.composes

import android.graphics.Paint.Style
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
fun ArrestedCompose(
    navController: NavController,
    gameViewModel: GameViewModel
) {
    val width = LocalConfiguration.current.screenWidthDp.dp           // This variable is used to hold current screen width in dp
    val height = LocalConfiguration.current.screenHeightDp.dp         // This variable is used to hold current screen height in dp

    val time = remember {
        Animatable(0f)
    }
    LaunchedEffect(Unit) {
        time.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 10000, easing = LinearEasing))
        gameViewModel.beginVoting()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black500),
    ) {
        Image(
            painter = painterResource(id = R.drawable.police_tapes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .size(width = width, height = (height.value * 0.65).dp)
                .offset(0.dp, ((height.value / 6) + 10).dp),
            contentScale = ContentScale.FillBounds
        )
        Box(modifier = Modifier) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp, bottom = 170.dp)
                    .offset(0.dp, -height / 20),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.handcuffs),
                    contentDescription = null,
                    modifier = Modifier
                        .size(280.dp, 200.dp),
                    contentScale = ContentScale.FillBounds
                )
                Box(
                    modifier = Modifier
                        .size(width = 320.dp, height = 280.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val killedPlayer = gameViewModel.latestKilled
                    val deadInfoText =
                        if (killedPlayer.value == gameViewModel.game.player!!.nickname) {
                            "YOU ARE"
                        } else if (killedPlayer.value == "") {
                            "NOBODY IS"
                        } else {
                            "${killedPlayer.value} IS"
                        }

                    Text(
                        text = deadInfoText,
                        color = Red500,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.anton_regular)),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "ARRESTED",
                        color = Red500,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.anton_regular)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 70.sp,
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(top = 150.dp)
                    )
                }
            }
        }

//        drops.forEachIndexed { index, value ->
//            Image(
//                painter = painterResource(id = R.drawable.blood_drop2),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(20.dp)
//                    .offset(width * (index + 1) / (drops.size + 1), height * value.value),
//                contentScale = ContentScale.FillBounds
//            )
//        }
//
//        Image(
//            painter = painterResource(id = R.drawable.blood8),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth()
//                .offset(0.dp, 0.dp)
//                .width(width)
//                .height(height / 1.5f),
//            contentScale = ContentScale.FillBounds
//        )
        Image(
            painter = painterResource(id = R.drawable.frame1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}