package com.example.mafia.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafia.R
import com.example.mafia.ui.theme.Black200
import com.example.mafia.ui.theme.Red500

class Player(val nickname: String, val role: Role, var lifeStatus: LifeStatus = LifeStatus.ALIVE) {
    var voteCounter : Int = 0

    @Composable
    fun votePlayer(
    ) {
        var imageForIcon : Int
        var scale: Float
        var padding: Dp
        if (nickname.last() == 'A'){
            imageForIcon = R.drawable.hat3
            scale = 1.20f
            padding = 0.dp
        }
        else{
            imageForIcon = R.drawable.hat4
            scale = 1.0f
            padding = 8.dp
        }

        Box(modifier = Modifier
            .size(300.dp)
            .padding(end = 8.dp)) {
            Image(painter = painterResource(id = R.drawable.frame1), contentDescription = null, modifier = Modifier
                .fillMaxSize()
                .background(
                    Black200,
                    shape = RoundedCornerShape(30.dp)
                )
                .clip(RoundedCornerShape(30.dp)),
                contentScale = ContentScale.FillBounds)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Image(
                    painter = painterResource(id = imageForIcon),
                    contentDescription = null,
                    modifier = Modifier.size(130.dp * scale)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = nickname,
                    modifier = Modifier.height(300.dp - 130.dp * scale - 30.dp),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp),
                    fontFamily = FontFamily(Font(R.font.anton_regular)),
                    color = Color.White
                )
            }
        }

    }


}
@Composable
fun showPlayer(
    nickname: String = "Lipek",
    size: Dp = 200.dp
) {
    var imageForIcon : Int
    var scale: Float
    var padding: Dp
    if (nickname.last() == 'A'){
        imageForIcon = R.drawable.hat3
        scale = 1.20f
        padding = 0.dp
    }
    else{
        imageForIcon = R.drawable.hat4
        scale = 1.0f
        padding = 8.dp
    }

    Box(modifier = Modifier
        .size(size)
        .padding(end = 8.dp)) {
        Image(painter = painterResource(id = R.drawable.frame6), contentDescription = null, modifier = Modifier
            .fillMaxSize()
            .background(
                Red500
            ),
            contentScale = ContentScale.FillBounds)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageForIcon),
                contentDescription = null,
                modifier = Modifier
                    .size((size / 2) * scale)
                    .padding(top = 20.dp / scale / scale)
            )
            Text(
                text = nickname,
                modifier = Modifier.padding(top = padding),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                fontFamily = FontFamily(Font(R.font.anton_regular))
            )
        }
    }

}

@Composable
fun deathNote(
) {
    var scale: Float
    if ("TOMEK".last() == 'A'){
        scale = 1.20f
    }
    else{
        scale = 1.0f
    }

    Box(modifier = Modifier
        .size(300.dp)
        .padding(end = 8.dp)) {
        Image(painter = painterResource(id = R.drawable.frame1), contentDescription = null, modifier = Modifier
            .fillMaxSize()
            .background(
                Black200,
                shape = RoundedCornerShape(30.dp)
            )
            .clip(RoundedCornerShape(30.dp)),
            contentScale = ContentScale.FillBounds)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.skull1),
                contentDescription = null,
                modifier = Modifier.size(130.dp * scale)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "TOMEK",
                modifier = Modifier.height(300.dp - 130.dp * scale - 30.dp),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp),
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                color = Color.Black
            )
        }
    }

}