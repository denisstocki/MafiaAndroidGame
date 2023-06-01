package com.example.mafia.composes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.mafia.R
import com.example.mafia.navigation.NavigationRoutes
import com.example.mafia.ui.theme.Grey200
import com.example.mafia.ui.theme.Red500
import com.example.mafia.viewmodel.GameViewModel

@Composable
fun DialogCompose(
    navController: NavController,
    type: DialogType,
    width: Dp,
    gameViewModel: GameViewModel,
    waitingPin: MutableState<Boolean>,
    gamePin: MutableState<String>,
    onDismiss: () -> Unit
) {

    val dialogWidth = width * 2 / 3
    val dialogHeight = dialogWidth * 2

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .width(dialogWidth)
                .height(dialogHeight)
                .clip(RoundedCornerShape(30.dp))
                .border(width = 5.dp, shape = RoundedCornerShape(30.dp), color = Grey200)
                .background(Color.Black)
        ) {

            if (type == DialogType.JOIN) {
                JoinCompose(
                    dialogWidth,
                    dialogHeight,
                    navController,
                    gameViewModel
                )
            } else {
                CreateCompose(
                    dialogWidth,
                    dialogHeight,
                    navController,
                    gameViewModel,
                    waitingPin,
                    gamePin
                )
            }

            Image(
                painter = painterResource(id = R.drawable.frame1),
                contentDescription = null,
                modifier = Modifier
                    .width(dialogWidth)
                    .height(dialogHeight)
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
fun CreateCompose(
    w: Dp,
    h: Dp,
    navController: NavController,
    gameViewModel: GameViewModel,
    waitingPin: MutableState<Boolean>,
    gamePin: MutableState<String>
) {
    val heightBigger = (h - w * 2 / 12) * 3 / 13
    val heightLower = (h - w * 2 / 12) * 2 / 13

    val blockedCharacters = setOf(' ', '-', ',', '.', '\n')
    var text: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var nickame: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = w / 6, bottom = w / 6)
            .background(Red500),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomLabel(
            text = "GAME PIN",
            height = heightBigger,
            backgroundColor = Color.Black,
            textColor = Red500
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightLower)
                .background(Red500),
            contentAlignment = Alignment.Center
        ) {
            if(!waitingPin.value){
                Text(
                    text = "Waiting...",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Normal)),
                        fontSize = 35.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Red500)
                )
            }
            if (!gamePin.value.isEmpty()){
                Text(
                    text = gamePin.value,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Normal)),
                        fontSize = 35.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Red500)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightLower)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NICKNAME",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Normal)),
                    fontSize = 35.sp,
                    color = Red500
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .background(Red500)
                .fillMaxWidth()
                .height(heightLower),
            value = nickame,
            placeholder = {
                Text(
                    text = "....",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontFamily = FontFamily(Font(R.font.anton_regular)),
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = { txt ->
                if (txt.text.length <= 12 && txt.text.all { !blockedCharacters.contains(it) }) {
                    nickame = txt
                }
            },
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightBigger)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "START",
                color = Red500,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Medium)),
                    fontSize = 35.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        gameViewModel.createPlayer(nickame.text, true)
                        navController.navigate(NavigationRoutes.Lobby.route)
                    }
            )
        }
    }
}

@Composable
fun CustomLabel(
    text: String,
    height: Dp,
    backgroundColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Normal)),
            fontSize = (height / 2).value.sp,
            color = textColor
        )
    }
}

@Composable
fun JoinCompose(
    w: Dp,
    h: Dp,
    navController: NavController,
    gameViewModel: GameViewModel
) {
    val heightBigger = (h - w * 2 / 6) * 3 / 13
    val heightLower = (h - w * 2 / 6) * 2 / 13

    val blockedCharacters = setOf(' ', '-', ',', '.', '\n')
    var gamePin by remember { mutableStateOf(TextFieldValue("")) }
    var nickame by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = w / 6, bottom = w / 6)
            .background(Red500),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightBigger)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "YOUR PIN",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                    fontSize = 35.sp,
                    color = Red500
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .background(Red500)
                .fillMaxWidth()
                .height(heightLower),
            value = gamePin,
            placeholder = {
                Text(
                    text = "....",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontFamily = FontFamily(Font(R.font.anton_regular)),
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { txt ->
                if (txt.text.length <= 4 && txt.text.all { !blockedCharacters.contains(it) }) {
                    gamePin = txt
                }
            },
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightBigger)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NICKNAME",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Normal)),
                    fontSize = 35.sp,
                    color = Red500
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .background(Red500)
                .fillMaxWidth()
                .height(heightLower),
            value = nickame,
            placeholder = {
                Text(
                    text = "....",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontFamily = FontFamily(Font(R.font.anton_regular)),
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = { txt ->
                if (txt.text.length <= 12 && txt.text.all { !blockedCharacters.contains(it) }) {
                    nickame = txt
                }
            },
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightBigger)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "JOIN",
                color = Red500,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Medium)),
                    fontSize = 35.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        var temp = gamePin
                        gamePin = TextFieldValue("Wait...")
                        Log.i("ELOELOELO", "jestem przed gameIsEmpty")
                        gameViewModel
                            .gameIsEmpty(temp.text)
                            .thenAccept { isEmpty ->
                                Log.i("ELOELOELO", "a nie zaczekalem sobie")
                                if (isEmpty) {
                                    gamePin = TextFieldValue("Err1")
                                } else {
                                    gamePin = temp
                                    temp = nickame
                                    nickame = TextFieldValue("Wait...")
                                    Log.i("ELOELOELO", "jestem przed gameIncludesNickame")

                                    gameViewModel
                                        .gameIncludesNickname(gamePin.text, temp.text)
                                        .thenAccept { isIncluded ->
                                            Log.i("ELOELOELO", "a nie zaczekalem sobie nickname")

                                            if (isIncluded) {
                                                nickame = TextFieldValue("Used")
                                            } else {
                                                nickame = temp
                                                temp = gamePin
                                                gamePin = TextFieldValue("Wait...")
                                                gameViewModel
                                                    .gameIsStarted(temp.text)
                                                    .thenAccept { isStarted ->
                                                        Log.i(
                                                            "ELOELOELO",
                                                            "a nie zaczekalem sobie nickname"
                                                        )

                                            if (isStarted) {
                                                gamePin = TextFieldValue("Err2")
                                            } else {
                                                gamePin = temp
                                                gameViewModel.joinToGame(gamePin = gamePin.text)
                                                gameViewModel.createPlayer(nickame.text)
                                                gameViewModel.assignListenerForGameStatus(navController)
                                                navController.navigate(NavigationRoutes.Lobby.route)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            )
        }
    }
}
