package com.nhatvm.toptop.data.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.components.CommentScreen
import com.nhatvm.toptop.data.components.Header
import com.nhatvm.toptop.data.components.OptionBar
import com.nhatvm.toptop.data.components.TabBar
import com.nhatvm.toptop.data.components.VideoInterface
import com.nhatvm.toptop.data.components.VideoPlayer

@Composable
fun HomeScreen(navController: NavHostController){
    var OptionScreen by remember { mutableStateOf(Routes.OPTION_SCREEN) }
    var CommentScreen by remember { mutableStateOf(false) }
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ){
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
                ){
                    items(5){
                        Box(
                            modifier = Modifier
                                .height(LocalConfiguration.current.screenHeightDp.dp - 60.dp)
                        ){
                            Column (
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black)
                            ){
                                VideoPlayer(R.raw.choingu1)
                            }
                            VideoInterface(
                                username = "ductihong82",
                                content = "Tao là bố của chúng mày",
                                hastag = listOf("#calisthenic", "#top1vku"),
                                songname = "Avicii - Waiting for love",
                                onAvatarClick = {},
                                onLikeClick = {},
                                onCommentClick = {},
                                onShareClick = {},
                            )
                        }
                    }
                }
                Box(modifier = Modifier.padding(top = 40.dp)
                ){
//                    Header(isTabSelectedIndex = 1)
                }
            }
            Box(){
                TabBar(navController)
            }
        }
        if (OptionScreen){
            OptionBar(OptionScreen){
                OptionScreen = it
            }
        }
        if (CommentScreen){
            CommentScreen(CommentScreen){
                CommentScreen = it
            }
        }
    }
}


