package com.nhatvm.toptop.data.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhatvm.toptop.data.R

@Composable
fun CommentScreen(videoId: Int, hideCommentScreen: () -> Unit) {
    Column (modifier = Modifier.fillMaxWidth().height(500.dp)){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    hideCommentScreen
                }
        ){
        }
        Column(
            modifier = Modifier
                .background(Color.White)
        ){
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ){
                Spacer(modifier = Modifier)
                Text(
                    text = "579 comments",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier)
            }
            LazyColumn{
                items(10){
                    CommentBar(
                        image = R.drawable.test_avtuser,
                        username = "ductihong82",
                        comment = "Video with id: $videoId",
                        time = "20h",
                        like = "2000"
                    )
                }
            }
        }
    }
}

@Composable
fun CommentBar(image: Int, username: String, comment: String, time: String, like: String){
    Row (
        Modifier
            .padding(15.dp, 0.dp)
            .fillMaxWidth()
    ){
        CircleImage(image = image, size = 40.dp)
        Spacer(modifier = Modifier.size(10.dp))
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Column (

            ){
                Text(
                    text = username,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                Row (
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(0.dp, 3.dp)
                ){
                    Text(
                        text = comment, fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = time, fontSize = 15.sp, color = Color.Gray
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "View replies (4)",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                    Image(
                        painter = painterResource(id = R.drawable.down_icon),
                        contentDescription = ""
                    )
                }
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = R.drawable.likecomment_icon),
                    contentDescription = ""
                )
                Text(text = like, fontSize = 18.sp, color = Color.Gray)
            }
        }
    }
    Spacer(modifier = Modifier.size(15.dp))
}