package com.example.mafia.composes

import android.app.Activity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mafia.R
import com.example.mafia.navigation.NavigationRoutes
import com.example.mafia.ui.theme.Grey200
import com.example.mafia.ui.theme.Red500
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.mafia.ui.theme.Black200
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WelcomeAnimation(
    navController: NavHostController
) {
    val offsetX = remember { Animatable(-400f) }

    ChangeStatusBarColor(color = Black200, darkIcons = false)

    LaunchedEffect(Unit) {
        offsetX.animateTo(0f, tween(1500, easing = LinearOutSlowInEasing))
        delay(500)
        navController.popBackStack()
        navController.navigate(NavigationRoutes.Start.route)
    }

    WelcomeCompose(offsetX)
}

@Composable
fun WelcomeCompose(
    offsetX: Animatable<Float, AnimationVector1D>
) {
    val width = LocalConfiguration.current.screenWidthDp.dp
    val height = LocalConfiguration.current.screenHeightDp.dp

    val bottomImage = painterResource(id = R.drawable.blood9)

    val bottomImH = with(LocalDensity.current) {
        bottomImage.intrinsicSize.height.toDp()
    }

    val bottomImW = with(LocalDensity.current) {
        bottomImage.intrinsicSize.width.toDp()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Red500)
    ) {
        Image(
            painter = bottomImage,
            contentDescription = null,
            modifier = Modifier
                .offset(0.dp, height - width * bottomImH.value / bottomImW.value)
                .fillMaxWidth()
                .size(width)
        )
        Image(
            painter = painterResource(id = R.drawable.blood8),
            contentDescription = null,
            modifier = Modifier
                .offset(0.dp, 0.dp)
                .fillMaxWidth()
                .size(width)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = height / 5, bottom = height / 5),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .size(width * 2 / 5)
                    .background(Grey200, CircleShape)
                    .offset(x = -offsetX.value.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((width / 40).value.dp)
                        .background(Color.Black, CircleShape)
                        .align(Alignment.Center)
                        .offset(x = -offsetX.value.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.mafia1),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                    )
                }
            }
            Text(
                text = "MAFIA GAME",
                color = Color.Black,
                fontSize = (height / 12).value.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                modifier = Modifier
                    .offset(x = offsetX.value.dp)
            )
        }
    }
}

