package com.nhatvm.toptop.data.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.video.repository.Comment
import com.nhatvm.toptop.data.video.repository.VideoRepository

@Composable
fun CommentScreen(videoId: Int, onComment: (String) -> Unit) {
    var comments by remember {
        mutableStateOf<List<Comment>>(listOf())
    }
    LaunchedEffect(Unit){
        comments = VideoRepository().getVideoObject()[videoId].commentVideo
    }

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
    ){
        Column(
            modifier = Modifier
                .background(Color(245, 243, 248))
        ){
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp)
            ){
                Spacer(modifier = Modifier)
                Text(
                    text = "${comments.size} comments",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier)
            }
            LazyColumn{
                items(comments){ comment->
                    CommentBar(
                        image = R.drawable.test_avtuser,
                        username = "${comment.userComment.Name}",
                        comment = "${comment.comment}",
                        time = "${comment.timeComment}",
                        like = "${comment.likeComment}"
                    )
                }
            }
        }
        CommentInput(modifier = Modifier.align(Alignment.BottomEnd), onComment)
    }
}

@Composable
fun CommentBar(image: Int, username: String, comment: String, time: String, like: String){
    Row (
        modifier = Modifier
            .padding(15.dp, 0.dp)
            .fillMaxWidth()
    ){
        CircleImage(image = image, size = 40.dp, modifier = Modifier.padding(top = 10.dp))
        Spacer(modifier = Modifier.size(10.dp))
        Column (modifier = Modifier.fillMaxWidth()){
            Text(
                text = username,
                fontSize = 15.sp,
                color = Color.Gray
            )
            Text(
                text = comment, fontSize = 16.sp
            )
            Row (
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = time, fontSize = 15.sp, color = Color.Gray,
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Image(
                        painter = painterResource(id = R.drawable.likecomment_icon),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(text = like, fontSize = 18.sp, color = Color.Gray)
                }
            }
        }
    }
    Spacer(modifier = Modifier.size(15.dp))
}

@Composable
fun CommentInput(modifier: Modifier = Modifier, onComment:(String) -> Unit) {
    var input by remember {
        mutableStateOf("")
    }
    Row (
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp)
    ){
        CircleImage(image = R.drawable.test_avtuser, size = 40.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(245, 243, 248))
                .width(310.dp)
                .height(50.dp)
                .padding(10.dp)

        ) {
            Spacer(modifier = Modifier.size(5.dp))
            BasicTextField(
                value = input,
                onValueChange = {it->input=it},
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.sendcomment_icon),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onComment(input)
                    }
            )
        }
    }
}