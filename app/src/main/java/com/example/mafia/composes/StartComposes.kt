package com.example.mafia.composes

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.mafia.R
import com.example.mafia.navigation.NavigationRoutes
import com.example.mafia.ui.theme.Black200
import com.example.mafia.ui.theme.Grey200
import com.example.mafia.ui.theme.Red500
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun StartCompose(
    navController: NavHostController
) {

    val width = LocalConfiguration.current.screenWidthDp.dp           // This variable is used to hold current screen width in dp
    val height = LocalConfiguration.current.screenHeightDp.dp         // This variable is used to hold current screen height in dp

    val used = remember { Animatable(width.value + 100f) }
    val used1 = remember { Animatable(-100f) }
    val colo = remember {
        Animatable(Color.Black)
    }
    val colo1 = remember {
        Animatable(Color.Black)
    }

    LaunchedEffect(Unit) {
        with(used) {             // Explains what the animation needs to follow
        delay(1000L)
            animateTo(                                                // Describes animations params
                targetValue = 0f,                                     // Describes target value of variable named offsetX
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 5000
                        width.value + 100f at 0 with LinearEasing
                        -100f at 5000 with LinearEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        with(used1) {             // Explains what the animation needs to follow
        delay(700L)
            animateTo(                                                // Describes animations params
                targetValue = width.value + 100f,                                     // Describes target value of variable named offsetX
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 5000
                        -100f at 0 with LinearEasing
                        width.value + 100f at 5000 with LinearEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            colo.animateTo(Color.Yellow, animationSpec = tween(200, easing = LinearEasing))
            colo.animateTo(Color.Black, animationSpec = tween(200, easing = LinearEasing))
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            colo1.animateTo(Red500, animationSpec = tween(350, easing = LinearEasing))
            colo1.animateTo(Color.Black, animationSpec = tween(350, easing = LinearEasing))
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Black200),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "YOUR CHOICE",
            color = Red500,
            fontSize = 50.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.anton_regular)),
            modifier = Modifier
                .padding(top = 16.dp)
        )
        var newGame by remember {
            mutableStateOf(false) // Initially dialog is closed
        }

        ButtonClick(buttonText = "NEW GAME", 70) {
            newGame = true
        }

        if (newGame) {
            NewGameCompose (navController){
                newGame = false
            }
        }

        var joinGame by remember {
            mutableStateOf(false) // Initially dialog is closed
        }

        ButtonClick(buttonText = "JOIN GAME", 20) {
            joinGame = true
        }

        if (joinGame) {
            JoinGameCompose (navController){
                joinGame = false
            }
        }
    }
    Image(
        painter = painterResource(id = R.drawable.town1),
        contentDescription = null,
        modifier = Modifier
            .size(width)
            .offset(y = height - 353.dp)
    )
    Box(modifier = Modifier
        .size(80.dp)
        .offset(x = used.value.dp, y = 380.dp),
    contentAlignment = Alignment.Center) {
    Image(painter = painterResource(id = R.drawable.plane1), contentDescription = null, modifier = Modifier
        .fillMaxSize())
        Box(modifier = Modifier
            .size(2.dp)
            .background(color = colo.value)
            .align(Alignment.CenterStart)
            .clip(CircleShape))
        Box(modifier = Modifier
            .size(2.dp)
            .background(color = colo1.value)
            .align(Alignment.CenterEnd)
            .clip(CircleShape))

    }
    Box(modifier = Modifier
        .size(80.dp)
        .offset(x = used1.value.dp, y = 340.dp),
        contentAlignment = Alignment.Center) {
        Image(painter = painterResource(id = R.drawable.plane2), contentDescription = null, modifier = Modifier
            .fillMaxSize())
        Box(modifier = Modifier
            .size(2.dp)
            .background(color = colo.value)
            .align(Alignment.CenterEnd)
            .clip(CircleShape))
        Box(modifier = Modifier
            .size(2.dp)
            .background(color = colo1.value)
            .align(Alignment.CenterStart)
            .clip(CircleShape))

    }
}


@Composable
fun ButtonClick(
    buttonText: String,
    padding: Int,
    onButtonClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .padding(top = padding.dp)
            .height(80.dp)
            .border(5.dp, Color.White, shape = RoundedCornerShape(20.dp))
            .background(color = Grey200, shape = RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.textButtonColors(contentColor = Color.White),
        onClick = {
            onButtonClick()
        }
    ) {
        Text(
            text = buttonText,
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.anton_regular))
        )
    }

}
