package com.example.mafia.composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.mafia.R
import com.example.mafia.navigation.NavigationRoutes
import com.example.mafia.ui.theme.Grey200
import com.example.mafia.ui.theme.Red500

@Composable
fun NewGameCompose(navController: NavController, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Grey200)
        ) {

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = 0.dp, y = 0.dp),
                    painter = painterResource(id = R.drawable.blood8),
                    contentDescription = "Mafia",
                    alignment = Alignment.Center
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.offset(x = 0.dp, y = 0.dp)
                ) {
                    Text(
                        text = "GAME PIN:",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                            fontSize = 35.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
                    )

                    Text(
                        text = "9302",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Normal)),
                            fontSize = 35.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth().padding(top = 25.dp)
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth().padding(top = 37.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Red500),
                        onClick = {
                            navController.navigate(NavigationRoutes.Lobby.route)
                        }
                    ) {
                        Text(
                            text = "START",
                            color = Color.White,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Medium)),
                                fontSize = 30.sp
                            )
                        )
                    }

            }
        }
    }
}