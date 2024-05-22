package com.nhatvm.toptop.data.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhatvm.toptop.data.R

@Composable
fun ShareBar(videoId: Int, hideShareBar: () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ){
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            Text(
                text = "Share to",
                color = Color.Black,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
        LineColor(color = Color.LightGray, modifier = Modifier.padding(15.dp))
        Row (
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ){
            OptionItem(image = R.drawable.report_icon, text = "Report")
            OptionItem(image = R.drawable.unheart_icon, text = "Not intested")
            OptionItem(image = R.drawable.download_icon, text = "Save video")
            OptionItem(image = R.drawable.duet_icon, text = "Duet")
            OptionItem(image = R.drawable.react_icon, text = "React")
        }
        LineColor(color = Color.LightGray, modifier = Modifier.padding(0.dp, 10.dp))
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Cancel",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .clickable {
                        hideShareBar()
                    }
            )
        }
    }
}

@Composable
fun OptionItem(image:Int, text:String){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ){
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(CircleShape)
                .size(45.dp)
                .background(Color.LightGray)
        ){
            Image(
                painter = painterResource(id = image),
                contentDescription = "",
            )
        }
        Text(
            text = text,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}