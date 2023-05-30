package com.example.mafia.composes

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mafia.R
import com.example.mafia.elements.Utility.playerList
import com.example.mafia.elements.showPlayer
import com.example.mafia.firebaseData.dbPlayer
import com.example.mafia.navigation.NavigationRoutes
import com.example.mafia.ui.theme.Grey200
import com.example.mafia.ui.theme.Red500
import com.example.mafia.viewmodel.GameViewModel

@Composable
fun LobbyCompose(
    navController: NavController,
    gameViewModel: GameViewModel,
){
    val playerList = gameViewModel.playerList

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically) {
            Text(text = "LEAVE", textAlign = TextAlign.Center, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                fontSize = 20.sp
            ),
                color = Red500)
        Text(
            text = "GAME",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                fontSize = 40.sp
            ),
            color = Color.White
        )
            Text(text = "START", textAlign = TextAlign.Center, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Bold)),
                fontSize = 20.sp
            ),
                color = Red500)
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Red500)
                    .clickable {
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
                    fontSize = 40.sp
                ),
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Red500)
                    .clickable {
                        navController.navigate(NavigationRoutes.Loading.route)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.uzi1),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
        Divider()
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                text = "PLAYERS:",
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.anton_regular))
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "${playerList.size}",
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Grey200)) {

            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            ) {

                itemsIndexed(playerList.chunked(3)) { _, rowPlayers ->
                    LazyRow(
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        items(rowPlayers) { player ->
                           showPlayer(player.nickname.toString(),100.dp)
                        }
                    }
                    Divider()
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