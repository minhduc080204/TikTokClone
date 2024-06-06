package com.nhatvm.toptop.data.inbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.components.CircleImage
import com.nhatvm.toptop.data.components.TextBold

@Preview(showSystemUi = true)
@Composable
fun InboxScreen() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ){
            TextBold(username = "Inbox", color = Color.Black, fontsize = 20.sp)
        }
        LazyColumn(){
            items(15){
                InboxItem("")
            }
        }

    }
}

@Composable
fun InboxItem(imageUrl: String) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ){
        CircleImage(
            imageUrl = imageUrl,
            size = 55.dp
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column {
            TextBold(username = "bichloan", color = Color.Black, fontsize = 18.sp)

            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "đã gửi tin nhắn cho bạn", fontSize = 16.sp)
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = "- 5 giờ", color = Color.LightGray)
            }
        }
    }
}