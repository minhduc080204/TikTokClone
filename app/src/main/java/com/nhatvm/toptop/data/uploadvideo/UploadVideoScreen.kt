package com.nhatvm.toptop.data.uploadvideo

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import com.nhatvm.toptop.data.video.LOADING

@UnstableApi
@Composable
fun UploadVideoScreen(
    videoUri: Uri,
    onUpload:(String, List<String>, String) -> Unit,
    onBack:() -> Unit,
    onChose:() -> Unit,
    isLoading: Boolean,
) {
    var content by remember {
        mutableStateOf("")
    }
    var hashtag by remember {
        mutableStateOf("")
    }
    var song by remember {
        mutableStateOf("")
    }
    if(isLoading){
        LOADING("Video is uploading")
    }else{
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(10.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
            ){
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(35.dp)
                        .clickable {
                            onBack()
                        }
                )
            }
            Row (modifier = Modifier.weight(1f)){
                Column ( modifier = Modifier.weight(1f)){
                    OutlinedTextField(
                        value = content, onValueChange = {it -> content= it},
                        placeholder = { Text(text = "Thêm mô tả...") },
                        maxLines = 15,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                    OutlinedTextField(
                        value = hashtag, onValueChange = {it -> hashtag= it},
                        placeholder = { Text(text = "Thêm Hastag...") },
                        maxLines = 10,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                    OutlinedTextField(
                        value = song, onValueChange = {it -> song= it},
                        placeholder = { Text(text = "Tên bài hát...") },
                        maxLines = 10,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column(modifier = Modifier
                    .width(150.dp)
                ){
                    VideoPlayer(uri = videoUri)
                    Button(onClick = { onChose }) {
                        Text(text = "Chose video")
                    }
                }
            }
            Button(
                onClick = {
                    val listHashtag = hashtag.split("#").map { it.trim() }.filter { it.isNotEmpty() }
                    onUpload(content, listHashtag, song)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White,
                )
            ) {
                Text(
                    text = "Upload",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun VideoPlayer(uri: Uri?) {
    Box(
        modifier = Modifier.width(150.dp).height(300.dp),
        content = {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxSize()
            ) {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setBackgroundColor(Color.Transparent.toArgb())
                            setVideoURI(uri)
                            setOnPreparedListener { mp ->
                                mp.isLooping = true
                            }
                            start()
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}