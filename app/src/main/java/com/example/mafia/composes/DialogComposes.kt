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
                .clip(RoundedCornerShape(width / 12))
                .border(width = 5.dp, shape = RoundedCornerShape(width / 12), color = Grey200)
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
    val heightBigger = (h - w / 3) * 3 / 13
    val heightLower = (h - w / 3) * 2 / 13

    val blockedCharacters = setOf(' ', '-', ',', '.', '\n')
    var nickname by remember { mutableStateOf("") }

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
        ) {

        }

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
                        fontSize = (heightLower / 3).value.sp,
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
                        fontSize = (heightLower / 3).value.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Red500)
                )
            }
        }

        CustomLabel(
            text = "NICKNAME",
            height = heightBigger,
            backgroundColor = Color.Black,
            textColor = Red500
        ) {

        }

        CustomTextField(
            backgroundColor = Red500,
            textColor = Color.Black,
            value = nickname,
            height = heightLower,
            keyboardType = KeyboardType.Text,
            onChange = { txt ->
                if (txt.length <= 11 && txt.all { !blockedCharacters.contains(it) }) {
                    nickname = txt.uppercase()
                }
            }
        )

        CustomLabel(
            text = "START",
            height = heightBigger,
            backgroundColor = Color.Black,
            textColor = Red500
        ) {
            gameViewModel.createPlayer(nickname, true)
            navController.navigate(NavigationRoutes.Lobby.route)
        }
    }
}

@Composable
fun CustomLabel(
    text: String,
    height: Dp,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
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
                .background(Color.Transparent)
                .clickable {
                    onClick()
                },
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Normal)),
            fontSize = (height / 2).value.sp,
            color = textColor
        )
    }
}

@Composable
fun CustomTextField(
    backgroundColor: Color,
    textColor: Color,
    value: String,
    height: Dp,
    keyboardType: KeyboardType,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(height),
        value = value,
        placeholder = {
            Text(
                text = "....",
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.anton_regular)),
            fontSize = (height / 3).value.sp,
            color = textColor,
            textAlign = TextAlign.Center
        ),
        onValueChange = { txt ->
            onChange(txt)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = textColor,
            unfocusedBorderColor = textColor,
            cursorColor = textColor
        )
    )
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
    var gamePin by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = w / 6, bottom = w / 6)
            .background(Red500),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomLabel(
            text = "YOUR PIN",
            height = heightBigger,
            backgroundColor = Color.Black,
            textColor = Red500
        ) {}

        CustomTextField(
            backgroundColor = Red500,
            textColor = Color.Black,
            value = gamePin,
            height = heightLower,
            keyboardType = KeyboardType.Number,
            onChange = { txt ->
                if (txt.length <= 4 && txt.all { !blockedCharacters.contains(it) }) {
                    gamePin = txt
                }
            }
        )

        CustomLabel(
            text = "NICKNAME",
            height = heightBigger,
            backgroundColor = Color.Black,
            textColor = Red500
        ) {}

        CustomTextField(
            backgroundColor = Red500,
            textColor = Color.Black,
            value = nickname,
            height = heightLower,
            keyboardType = KeyboardType.Text,
            onChange = { txt ->
                if (txt.length <= 11 && txt.all { !blockedCharacters.contains(it) }) {
                    nickname = txt.uppercase()
                }
            }
        )

        CustomLabel(
            text = "JOIN",
            height = heightBigger,
            backgroundColor = Color.Black,
            textColor = Red500
        ) {
            var temp = gamePin
            gamePin = "Wait..."
            gameViewModel
                .gameIsEmpty(temp)
                .thenAccept { isEmpty ->
                    if (isEmpty) {
                        gamePin = "Err1"
                    } else {
                        gamePin = temp
                        temp = nickname
                        nickname = "Wait..."

                        gameViewModel
                            .gameIncludesNickname(gamePin, temp)
                            .thenAccept { isIncluded ->

                                if (isIncluded) {
                                    nickname = "Used"
                                } else {
                                    nickname = temp
                                    temp = gamePin
                                    gamePin = "Wait..."
                                    gameViewModel
                                        .gameIsStarted(temp)
                                        .thenAccept { isStarted ->

                                            if (isStarted) {
                                                gamePin = "Err2"
                                            } else {
                                                gamePin = temp
                                                gameViewModel.joinToGame(gamePin = gamePin)
                                                gameViewModel.createPlayer(nickname)
                                                gameViewModel.assignListenerForGameStatus(navController)
                                                navController.navigate(NavigationRoutes.Lobby.route)
                                            }
                                        }
                                }
                            }
                    }
                }
        }
    }
}
