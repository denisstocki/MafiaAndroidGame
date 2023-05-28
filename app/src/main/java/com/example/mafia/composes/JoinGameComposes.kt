package com.example.mafia.composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
fun JoinGameCompose(navController: NavController, onDismiss: () -> Unit) {
    var text: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val blockedCharacters = setOf(' ', '-', ',', '.', '\n')

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
                    text = "YOUR PIN:",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                        fontSize = 35.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
                )

                OutlinedTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    value = text,
                    placeholder = {
                        Text(
                            text = "....",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            fontFamily = FontFamily(Font(R.font.anton_regular)),
                            fontSize = 25.sp,
                            textAlign = TextAlign.Center) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                    onValueChange = { txt ->
                        if (txt.text.length <= 4 && txt.text.all { !blockedCharacters.contains(it) }) {
                            text = txt
                        }
                    },
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    ),
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