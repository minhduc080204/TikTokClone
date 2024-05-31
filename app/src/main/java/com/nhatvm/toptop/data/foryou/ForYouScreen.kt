package com.nhatvm.toptop.data.foryou

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.nhatvm.toptop.data.video.VideoDetailScreen
import com.nhatvm.toptop.data.video.VideoDetailViewModel
import com.nhatvm.toptop.data.video.repository.Video
import com.nhatvm.toptop.data.video.repository.VideoRepository

@UnstableApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForYouScreen(
    onShowComment: (Int) -> Unit,
    onShowShare: (Int) -> Unit,
) {
    val videoRepository = VideoRepository()
    var videoCount by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState()
    var listVideoInfor by remember { mutableStateOf<List<Video>>(listOf()) }
    LaunchedEffect(Unit) {
        videoCount = videoRepository.getVideoCount()
        listVideoInfor = videoRepository.getVideoObject()
    }
    VerticalPager(state = pagerState, pageCount = listVideoInfor.size) { videoId ->
        var isplay by remember {
            mutableStateOf(true)
        }
        if (videoId == pagerState.currentPage){
            isplay = true
        }else{
            isplay = false
        }

        val viewModel: VideoDetailViewModel = hiltViewModel(key = videoId.toString())
        VideoDetailScreen(
            videoId = videoId,
            videoinfor = listVideoInfor[videoId],
            viewModel = viewModel,
            onShowComment = onShowComment,
            onShowShare = onShowShare,
            isPlaying = isplay,
        )
    }
}