package com.example.mafia.composes

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafia.R
import com.example.mafia.elements.Role
import com.example.mafia.navigation.NavigationRoutes
import com.example.mafia.ui.theme.Red500
import com.example.mafia.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun LoadingCompose(
    navController: NavController,
    gameViewModel: GameViewModel
) {

    val width = LocalConfiguration.current.screenWidthDp.dp           // This variable is used to hold current screen width in dp
    val height = LocalConfiguration.current.screenHeightDp.dp         // This variable is used to hold current screen height in dp

    LaunchedEffect(key1 = true)
    {
        delay(2000)
        navController.navigate(NavigationRoutes.Day.route)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(width = 250.dp, height = 100.dp)
                    .background(Red500)
            ) {
                Text(
                    text = "YOUR ROLE",
                    textAlign = TextAlign.Center,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.anton_regular))
                )
                Image(
                    painter = painterResource(id = R.drawable.frame1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            when(gameViewModel.game.player!!.role){
                Role.MAFIA -> {
                    MafiaCompose()
                }
                Role.DETECTIVE -> {
                    DetectiveCompose()
                }
                Role.DOCTOR -> {
                    MedicCompose()
                }
                Role.CIVIL -> {
                    CivilCompose()
                }
                Role.EMPTY -> {

                }
                null -> Log.println(Log.ASSERT,"Test", "Pusta rola")
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(width = 250.dp, height = 140.dp)
                    .background(Red500)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "LOADING",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.anton_regular)),
                        color = Color.Black
                    )
                    LoadingAnimation()
                }
                Image(
                    painter = painterResource(id = R.drawable.frame1),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }


}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = Color.Black,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
)
{
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }
    val lastCircle = circleValues.size - 1

    Row(modifier = modifier)
    {
        circleValues.forEachIndexed { index, value ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .graphicsLayer {
                        translationY = -value * distance
                    }
                    .background(
                        color = circleColor,
                        shape = CircleShape
                    )
            )
            if(index != lastCircle)
            {
                Spacer(modifier = Modifier.width(spaceBetween))
            }
        }
    }
}