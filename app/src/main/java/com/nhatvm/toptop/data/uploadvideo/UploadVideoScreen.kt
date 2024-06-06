package com.nhatvm.toptop.data.uploadvideo

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.file.FileViewModel
import com.nhatvm.toptop.data.file.handleVideoUri1
import com.nhatvm.toptop.data.video.LOADING
import com.nhatvm.toptop.data.video.VideoDetailViewModel
import kotlinx.coroutines.coroutineScope

@Composable
fun UploadVideoScreen(
    videoUri: Uri,
    onUpload:(String, List<String>, String) -> Unit,
    onBack:() -> Unit, onChose:() -> Unit,
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
    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }
    thumbnail = ThumbnailUtils.createVideoThumbnail(videoUri.path ?: "", MediaStore.Images.Thumbnails.MINI_KIND)
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
                }
                Spacer(modifier = Modifier.size(10.dp))
                Column(modifier = Modifier
                    .width(150.dp)
                ){
                    thumbnail?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                        )
                    }
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