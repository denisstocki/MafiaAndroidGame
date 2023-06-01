package com.example.mafia.composes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mafia.R
import com.example.mafia.elements.showPlayer
import com.example.mafia.navigation.NavigationRoutes
import com.example.mafia.ui.theme.Black200
import com.example.mafia.ui.theme.Grey200
import com.example.mafia.ui.theme.Red500
import com.example.mafia.viewmodel.GameViewModel

@Composable
fun LobbyCompose(
    navController: NavController,
    gameViewModel: GameViewModel,
){
    val playerList = gameViewModel.playerList
    val myPlayerIsAdmin = gameViewModel.ifIamAdmin

    val width = LocalConfiguration.current.screenWidthDp.dp
    val height = LocalConfiguration.current.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .height(height * 2 / 5)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height * 2 / 15),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LEAVE",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                        fontSize = (height / 30).value.sp
                    ),
                    color = Red500
                )
                Text(
                    text = "GAME",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                        fontSize = (height / 20).value.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "START",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                        fontSize = (height / 30).value.sp
                    ),
                    color = Red500
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height * 2 / 15),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(height / 10)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Red500)
                        .clickable {
                            gameViewModel.removePlayer()
                            navController.navigate(NavigationRoutes.Start.route)
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.uzi2),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }
                Text(
                    text = gameViewModel.game.pin!!,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                        fontSize = (height / 20).value.sp
                    ),
                    color = Color.White
                )

                var backgroundColor: Color by remember {
                    mutableStateOf(Red500)
                }

                if (myPlayerIsAdmin.value) {
                    backgroundColor = Red500
                    Log.println(Log.ASSERT,"Test", "Czerwony")
                } else {
                    backgroundColor = Grey200
                    Log.println(Log.ASSERT,"Test", "szary")

                }

                Box(
                    modifier = Modifier
                        .size(height / 10)
                        .clip(RoundedCornerShape(8.dp))
                        .background(backgroundColor)
                        .clickable {
                            if (myPlayerIsAdmin.value) {
                                gameViewModel.assignRoles()
                                gameViewModel.assignListenerForGameStatus(navController)
                                gameViewModel.startGameForAll()
                            }
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.uzi1),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height * 2 / 15),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(
                    text = "PLAYERS:",
                    textAlign = TextAlign.Center,
                    fontSize = (height / 20).value.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.anton_regular))
                )

                Text(
                    text = "${playerList.size}",
                    textAlign = TextAlign.Center,
                    fontSize = (height / 20).value.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.anton_regular)),
                )
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .height(height * 3 / 5)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black200)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(width / 15),
                ) {
                    itemsIndexed(playerList.chunked(3)) { _, rowPlayers ->
                        LazyRow(
                            modifier = Modifier
                                .padding(
                                    vertical = (width / 15),
                                    horizontal = (width / 20)
                                )
                        ) {
                            items(rowPlayers) { player ->
                                Box(
                                    modifier = Modifier.size(height / 7)
                                        .padding(horizontal = 5.dp)
                                ) {
                                    showPlayer(player.nickname.toString(), height / 7)
                                }
                            }
                        }
                    }
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