package com.example.mafia.elements

import android.icu.text.ListFormatter.Width
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mafia.R
import com.example.mafia.firebaseData.dbPlayer
import com.example.mafia.ui.theme.Black200
import com.example.mafia.ui.theme.Gray200
import com.example.mafia.ui.theme.Red500

class Player(val nickname: String, val role: Role) {
    var voteCounter : Int = 0

    @Composable
    fun votePlayer(
        player: dbPlayer,
        vote: () -> Unit
    ) {
        var imageForIcon : Int
        var scale: Float
        var padding: Dp
        if (player.nickname!!.last() == 'A'){
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
            .clickable {
                vote()
            }
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
                    text = player.nickname!!,
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
    nickname: String,
    height: Dp
) {
    var imageForIcon : Int
    var scale: Float
    var padding: Dp

    if (nickname.last() == 'A'){
        imageForIcon = R.drawable.hat4
    }
    else{
        imageForIcon = R.drawable.hat4
    }

    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.frame6),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Red500),
            contentScale = ContentScale.FillBounds)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(height / 10),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageForIcon),
                contentDescription = null,
                modifier = Modifier.size(height/3),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = nickname,
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = (height / 7).value.sp),
                fontFamily = FontFamily(Font(R.font.anton_regular))
            )
        }
    }

}

@Preview
@Composable
fun deathNote(
    player: dbPlayer = dbPlayer("Lipek", Role.DOCTOR),
    fromRole: Role = Role.MAFIA
) {
    var scale: Float
    if (player.nickname!!.last() == 'A'){
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

        Text(
            text = "VOTED",
            modifier = Modifier
                .rotate(15f)
                .align(Alignment.Center)
                .padding(bottom = 20.dp),
            color = Red500,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Normal)),
                fontSize = 90.sp,
                letterSpacing = 6.sp
            )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            var image = when (fromRole) {
                Role.MAFIA -> {
                    painterResource(id = R.drawable.skull1)
                }
                Role.DOCTOR -> {
                    painterResource(id = R.drawable.shield2)
                }
                else -> {
                    painterResource(id = R.drawable.mafia_hat)
                }
            }

            if(fromRole == Role.DETECTIVE) {
                image = when (player.role) {
                    Role.MAFIA -> painterResource(id = R.drawable.mafia_icon)
                    Role.DETECTIVE -> painterResource(id = R.drawable.detective_glass)
                    Role.CIVIL -> painterResource(id = R.drawable.town_house)
                    Role.DOCTOR -> painterResource(id = R.drawable.medic1)
                    else -> {painterResource(id = R.drawable.mafia_hat)}
                }
            }
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.size(130.dp * scale)
            )
            Spacer(modifier = Modifier.height(35.dp))

            Text(
                text = player.nickname!!,
                modifier = Modifier.height(300.dp - 130.dp * scale - 30.dp),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp,letterSpacing = 2.sp ),
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                color = Gray200
            )
        }
    }
}
@Preview
@Composable
fun arrestedNote(
    player: dbPlayer = dbPlayer("Lipek"),
)
{
    var scale: Float
    if (player.nickname!!.last() == 'A'){
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

        Text(
            text = "VOTED",
            modifier = Modifier
                .rotate(15f)
                .align(Alignment.Center)
                .padding(bottom = 20.dp),
            color = Red500,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.anton_regular, FontWeight.Normal)),
                fontSize = 90.sp,
                letterSpacing = 6.sp
            )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.handcuffs),
                contentDescription = null,
                modifier = Modifier.size(160.dp * scale)
            )
//            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = player.nickname!!,
                modifier = Modifier.height(300.dp - 130.dp * scale - 30.dp),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 40.sp,letterSpacing = 2.sp ),
                fontFamily = FontFamily(Font(R.font.anton_regular)),
                color = Gray200
            )
        }
    }
}


@Composable
fun endGamePlayerShow(
    player: dbPlayer
) {
    var imageForIcon : Int
    if (player.nickname!!.last() == 'a'){
        imageForIcon = R.drawable.hat3
    }
    else{
        imageForIcon = R.drawable.hat4
    }
    Row(modifier = Modifier
        .padding(all = 8.dp)
        .wrapContentWidth(align = Alignment.Start)
    ) {
        Image(
            painter = painterResource(imageForIcon),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, Red500, CircleShape)
        )

        Spacer(modifier = Modifier.width(15.dp))

        Column {
            Text(
                text = player.nickname!!,
                color = Red500,
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = player.role.toString(),
                color = Color.Gray
            )
        }
    }
}
