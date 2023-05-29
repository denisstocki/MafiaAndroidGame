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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
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
fun StartAnimation(
    navController: NavHostController
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

    val offsetXC1 = remember { Animatable(Color.Black) }
    val offsetXC2 = remember { Animatable(Color.Black) }

    val bottomImage = painterResource(id = R.drawable.town1)

    val bottomImH = with(LocalDensity.current) { bottomImage.intrinsicSize.height.toDp() }
    val bottomImW = with(LocalDensity.current) { bottomImage.intrinsicSize.width.toDp() }

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

    LaunchedEffect(Unit) {
        while (true) {
            offsetXC1.animateTo(Color.Yellow, animationSpec = tween(200, easing = LinearEasing))
            offsetXC1.animateTo(Color.Black, animationSpec = tween(200, easing = LinearEasing))
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            offsetXC2.animateTo(Red500, animationSpec = tween(350, easing = LinearEasing))
            offsetXC2.animateTo(Color.Black, animationSpec = tween(350, easing = LinearEasing))
        }
    }

    StartCompose(
        planes,
        offsetXC1,
        offsetXC2,
        navController,
        width,
        height,
        bottomImW,
        bottomImH
    )
}

@Composable
fun StartCompose(
    planes: List<Animatable<Float, AnimationVector1D>>,
    offsetXC1: Animatable<Color, AnimationVector4D>,
    offsetXC2: Animatable<Color, AnimationVector4D>,
    navController: NavHostController,
    width: Dp,
    height: Dp,
    imageW: Dp,
    imageH: Dp
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
                    newGamePressed = true
                }
            }

            if (newGamePressed) {
                NewGameCompose(
                    navController
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
                JoinGameCompose(
                    navController
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

        val colors = listOf(
            offsetXC1,
            offsetXC2,
            offsetXC2,
            offsetXC1
        )

        for (i in planes.indices) {
            PlaneCompose(
                offsetX = planes[i],
                offsetXC1 = colors[(i * 2) % planes.size],
                offsetXC2 = colors[(i * 2 + 1) % planes.size],
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
    offsetXC1: Animatable<Color, AnimationVector4D>,
    offsetXC2: Animatable<Color, AnimationVector4D>,
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
                .background(color = offsetXC1.value)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
        )
        Box(
            modifier = Modifier
                .size(2.dp)
                .background(color = offsetXC2.value)
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
