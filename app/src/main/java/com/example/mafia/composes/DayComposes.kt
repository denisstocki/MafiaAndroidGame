package com.example.mafia.composes

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafia.R
import com.example.mafia.navigation.NavigationRoutes
import com.example.mafia.ui.theme.Grey200
import com.example.mafia.ui.theme.Red500
import com.example.mafia.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun DayCompose(
    navController: NavController,
    gameViewModel: GameViewModel,
    time: Float = 10*1000f
) {
    val width = LocalConfiguration.current.screenWidthDp.dp           // This variable is used to hold current screen width in dp
    val height = LocalConfiguration.current.screenHeightDp.dp         // This variable is used to hold current screen height in dp
    val remainingTime = remember { Animatable(time) }
    val rotationAngle = remember { Animatable(0f) }
    val color = remember { Animatable(Grey200) }
    val textColor = remember { Animatable(Color.White) }

    LaunchedEffect(Unit) {
        rotationAngle.animateTo(180f, animationSpec = tween(durationMillis = time.toInt(), easing = LinearEasing))
    }

    LaunchedEffect(Unit) {
        color.animateTo(Red500, animationSpec = tween(time.toInt(), easing = LinearEasing))
    }

    LaunchedEffect(Unit) {
        remainingTime.animateTo(0f, animationSpec = tween(durationMillis = time.toInt(), easing = LinearEasing))
    }

    LaunchedEffect(Unit) {
        textColor.animateTo(Red500, animationSpec = tween(time.toInt(), easing = LinearEasing))
        delay(1000)
        gameViewModel.beginVoting()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width)
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(width)
                    .align(Alignment.Center)
                    .rotate(rotationAngle.value)
                    .background(color = color.value),
                contentAlignment = Alignment.Center
            ) {
                // Obrazek Słońca
                Box(modifier = Modifier
                    .size(width - 40.dp)
                    .background(color = color.value)
                    .padding(40.dp)){
                    Image(
                        painter = painterResource(R.drawable.sun1),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopCenter)
                            .padding(top = 30.dp)
                    )

                    // Obrazek Ziemi
                    Image(
                        painter = painterResource(R.drawable.moon1),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 30.dp)
                    )
                }

            }
            Image(
                painter = painterResource(id = R.drawable.town1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .size(width = width, height = (height - width + 90.dp) / 2)
            .background(Color.Black),
        contentAlignment = Alignment.Center
        )
        {
            Text(
                text = "TIME TO TALK",
                textAlign = TextAlign.Center,
                color = Red500,
                style = MaterialTheme.typography.h6,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(modifier = Modifier
                .size(width = 45.dp, height = width - 90.dp)
                .background(Color.Black))
            Image(
                painter = painterResource(id = R.drawable.frame1),
                contentDescription = null,
                modifier = Modifier
                    .size(width - 90.dp),
                contentScale = ContentScale.FillBounds
            )
            Box(modifier = Modifier
                .size(width = 45.dp, height = width - 90.dp)
                .background(Color.Black))
        }
        Box(modifier = Modifier
            .size(width = width, height = (height - width + 90.dp) / 2)
            .background(Color.Black),
        contentAlignment = Alignment.Center)
        {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "REMAINING TIME",
                    textAlign = TextAlign.Center,
                    color = textColor.value,
                    style = MaterialTheme.typography.h6,
                    fontFamily = FontFamily(Font(R.font.anton_regular)),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(remainingTime.value / 1000).toInt()}s",
                    textAlign = TextAlign.Center,
                    color = textColor.value,
                    style = MaterialTheme.typography.h6,
                    fontFamily = FontFamily(Font(R.font.anton_regular)),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}
