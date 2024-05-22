//package com.nhatvm.toptop.data.test
//
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun VideoList(videos: List<Video>) {
//    LazyColumn {
//        items(videos) { video ->
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            ) {
//                Text(text = video.title)
//                VideoPlayer(video = video)
//            }
//        }
//    }
//}