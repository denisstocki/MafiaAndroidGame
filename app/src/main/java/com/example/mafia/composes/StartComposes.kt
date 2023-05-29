package com.example.mafia.composes

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavHostController
import com.example.mafia.R
import com.example.mafia.ui.theme.Black200
import com.example.mafia.ui.theme.Red500
import com.example.mafia.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun StartAnimation(
    navController: NavHostController,
    gameViewModel: GameViewModel
) {
    val width = LocalConfiguration.current.screenWidthDp.dp
    val height = LocalConfiguration.current.screenHeightDp.dp

    val planes = listOf(
        remember { Animatable(width.value + 100f) },
        remember { Animatable(-100f) },
        remember { Animatable(width.value + 100f) },
        remember { Animatable(-100f) }
    )

    val offsets = listOf(
        width.value + 100f,
        -100f,
        -100f,
        width.value + 100f
    )

    val delays = listOf(
        2000L,
        700L,
        200L,
        2700L
    )

    val colors = listOf(
        remember { Animatable(Color.Black) },
        remember { Animatable(Color.Black) }
    )

    val freqs = listOf(
        200,
        350
    )

    val colorsObjects = listOf(
        Color.Yellow,
        Color.Red
    )

    for (i in planes.indices) {
        LaunchedEffect(Unit) {
            with(planes[i % planes.size]) {
                delay(delays[i % planes.size])
                animateTo(
                    targetValue = offsets[(i * 2 + 1) % planes.size],
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = 5000
                            offsets[(i * 2) % planes.size] at 0 with LinearEasing
                            offsets[(i * 2 + 1) % planes.size] at 5000 with LinearEasing
                        },
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }
    }

    for (i in colors.indices) {
        LaunchedEffect(Unit) {
            while (true) {
                colors[i].animateTo(colorsObjects[i % 2], animationSpec = tween(freqs[i], easing = LinearEasing))
                colors[i].animateTo(Color.Black, animationSpec = tween(freqs[i], easing = LinearEasing))
            }
        }
    }
    val waitingPin = remember {
        mutableStateOf(false)
    }
    val gamePin = remember {
        mutableStateOf("")
    }

    StartCompose(
        planes,
        colors,
        navController,
        width,
        height,
        gameViewModel,
        waitingPin,
        gamePin
    ){
        gameViewModel.createGame(waitingPin,gamePin)
    }

}

@Composable
fun StartCompose(
    planes: List<Animatable<Float, AnimationVector1D>>,
    colors: List<Animatable<Color, AnimationVector4D>>,
    navController: NavHostController,
    width: Dp,
    height: Dp,
    gameViewModel: GameViewModel,
    waitingPin: MutableState<Boolean>,
    gamePin: MutableState<String>,
    createGame: () -> Unit
) {
    var newGamePressed by remember { mutableStateOf(false) }
    var joinGamePressed by remember { mutableStateOf(false) }
    Image(
        painter = painterResource(id = R.drawable.frame1),
        contentDescription = null,
        modifier = Modifier
            .width(width)
            .height(height)
            .offset(x = 0.dp, y = 0.dp),
        contentScale = ContentScale.FillBounds
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Black200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height / 2),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "YOUR CHOICE",
                color = Red500,
                fontSize = 50.sp, // Wish it does not damage the view on any phone
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
            )

            Box(
                modifier = Modifier
                    .width(width * 3 / 5)
                    .height(width / 5)
            ) {
                ClickableButton(
                    text = "NEW GAME",
                    Color.White
                ) {
                    createGame()
                    newGamePressed = true
                }
            }

            if (newGamePressed) {
                DialogCompose(
                    navController,
                    DialogType.CREATE,
                    width,
                    gameViewModel,
                    waitingPin,
                    gamePin,
                ) {
                    newGamePressed = false
                }
            }

            Box(
                modifier = Modifier
                    .width(width * 3 / 5)
                    .height(width / 5)
            ) {
                ClickableButton(
                    text = "JOIN GAME",
                    Color.White
                ) {
                    joinGamePressed = true
                }
            }

            if (joinGamePressed) {
                DialogCompose(
                    navController,
                    DialogType.JOIN,
                    width,
                    gameViewModel,
                    waitingPin,
                    gamePin
                ) {
                    joinGamePressed = false
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.town1),
            contentDescription = null,
            modifier = Modifier
                .width(width)
                .height(height / 4)
                .offset(x = 0.dp, y = height * 3 / 4),
            contentScale = ContentScale.FillBounds
        )

        val dirs = listOf(
            PlaneDir.LEFT,
            PlaneDir.RIGHT
        )

        val colorsSeq = listOf(
            colors[0],
            colors[1],
            colors[1],
            colors[0],
        )

        for (i in planes.indices) {
            PlaneCompose(
                offsetX = planes[i],
                color1 = colorsSeq[(i * 2) % planes.size],
                color2 = colorsSeq[(i * 2 + 1) % planes.size],
                width = width,
                height = height * (8 + i) / 16,
                dirs[i % 2]
            )
        }
    }
}

@Composable
fun PlaneCompose(
    offsetX: Animatable<Float, AnimationVector1D>,
    color1: Animatable<Color, AnimationVector4D>,
    color2: Animatable<Color, AnimationVector4D>,
    width: Dp,
    height: Dp,
    dir: PlaneDir
) {
    Box(
        modifier = Modifier
            .size(width / 8)
            .offset(x = offsetX.value.dp, y = height),
        contentAlignment = Alignment.Center
    ) {
        val planeImage = when (dir) {
            PlaneDir.LEFT -> painterResource(id = R.drawable.plane1)
            PlaneDir.RIGHT -> painterResource(id = R.drawable.plane2)
        }

        Image(
            painter = planeImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )
        Box(
            modifier = Modifier
                .size(2.dp)
                .background(color = color1.value)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
        )
        Box(
            modifier = Modifier
                .size(2.dp)
                .background(color = color2.value)
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
        )
    }
}

@Composable
fun ClickableButton(
    text: String,
    color: Color,
    action: () -> Unit
) {
    BoxWithConstraints {
        val halfHeight = maxHeight / 2
        val fontSize = with(LocalDensity.current) { halfHeight.value }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { action() }
                .border(5.dp, color = color, shape = RoundedCornerShape(20.dp))
                .background(color = Color.Black, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = color,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                textAlign = TextAlign.Center
            )
        }
    }
}

enum class PlaneDir {
    LEFT,
    RIGHT
}

enum class DialogType {
    CREATE,
    JOIN
}
