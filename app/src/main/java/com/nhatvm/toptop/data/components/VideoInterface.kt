package com.nhatvm.toptop.data.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.video.repository.Video

@Composable
fun VideoInterface(
    videoinfor: Video,
    onAvatarClick:() ->Unit,
    isLike: Boolean,
    onLikeClick:() ->Unit,
    onCommentClick:() ->Unit,
    onShareClick:() ->Unit,
){
    var likecount by remember {
        mutableStateOf(videoinfor.likeVideo)
    }
    Row (
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ){
        Column (
            modifier = Modifier.weight(1f)
        ){
            Row (verticalAlignment = Alignment.CenterVertically){
                TextBold(username = videoinfor.userVideo.Username)
                Image(
                    painter = painterResource(id = R.drawable.bluetick_icon),
                    contentDescription = "blue tick"
                )
            }
            TextWhite(videoinfor.contentVideo)
            TextBold("#${videoinfor.tagsVideo.joinToString(" #")}")
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(painter = painterResource(id =R.drawable.music_icon), contentDescription = "music")
                TextWhite(videoinfor.songVideo, 7.dp)
            }
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            CircleImage(
                imageUrl = videoinfor.userVideo.Image, size = 45.dp, border = 2.dp,
                modifier = Modifier
                    .clickable {onAvatarClick()}
            )
            Spacer(modifier = Modifier.size(30.dp))
            if (isLike){
                ActionVideoInterface(
                    painter = painterResource(id = R.drawable.heart_ok_icon),
                    contentDescription = "$videoinfor.",
                    number = "${likecount}",
                    onClick = {
                        onLikeClick()
                        likecount--
                    }
                )
            }else{
                ActionVideoInterface(
                    painter = painterResource(id = R.drawable.heart_icon),
                    contentDescription = "$videoinfor.",
                    number = "${likecount}",
                    onClick = {
                        onLikeClick()
                        likecount++
                    }
                )
            }
            ActionVideoInterface(
                painter = painterResource(id = R.drawable.comment_icon),
                contentDescription = "comment",
                number = "${videoinfor.commentVideo.size}",
                onClick = {onCommentClick()}
            )
            ActionVideoInterface(
                painter = painterResource(id = R.drawable.share_icon),
                contentDescription = "share",
                number = "${videoinfor.shareVideo}",
                onClick = {onShareClick()}
            )
            AudioTrackView(imageUrl = videoinfor.userVideo.Image)
        }
    }
}


@Composable
fun TextBold(username: String, color: Color = Color.White, fontsize: TextUnit = 16.sp){
    Text(
        text = username,
        color = color,
        fontSize = fontsize,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(end = 3.dp)
    )
}

@Composable
fun TextWhite(text:String, padding:Dp = 0.dp, fontSize:TextUnit = 16.sp){
    Text(
        text = text,
        color = Color.White,
        fontSize = fontSize,
        modifier = Modifier.padding(padding, top = 6.dp)
    )
}

@Composable
fun ActionVideoInterface(painter: Painter, contentDescription:String, number: String, onClick:() -> Unit){
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(32.dp)
            .clickable { onClick() }
    )
    TextWhite(text = number, fontSize = 13.sp)
    Spacer(modifier = Modifier.size(20.dp))
}

@Composable
fun AudioTrackView(
    modifier: Modifier = Modifier,
    imageUrl: String,
){
    val infiniteTransition = rememberInfiniteTransition()
    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = InfiniteRepeatableSpec(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            )
        )
    )
    CircleImage(
        imageUrl = imageUrl, size = 45.dp, border = 2.dp,
        modifier = modifier
            .rotate(rotate)
            .clickable {

            }
    )
}