package com.example.mafia.composes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafia.R
import com.example.mafia.ui.theme.Red500

@Preview
@Composable
fun DetectiveCompose(

) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Red500)
    ) {
        Image(
            painter = painterResource(id = R.drawable.frame1),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DETECTIVE",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Image(
                painter = painterResource(id = R.drawable.detective_glass),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}
@Preview
@Composable
fun MedicCompose(

) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Red500)
    ) {
        Image(
            painter = painterResource(id = R.drawable.frame1),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MEDIC",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Image(
                painter = painterResource(id = R.drawable.medic1),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}
@Preview
@Composable
fun MafiaCompose(

) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Red500)
    ) {
        Image(
            painter = painterResource(id = R.drawable.frame1),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MAFIA",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Image(
                painter = painterResource(id = R.drawable.glock1),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Preview
@Composable
fun CivilCompose(

) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Red500)
    ) {
        Image(
            painter = painterResource(id = R.drawable.frame1),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CIVIL",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Image(
                painter = painterResource(id = R.drawable.house1),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}