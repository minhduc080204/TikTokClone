package com.nhatvm.toptop.data.Screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.nhatvm.toptop.data.video.VideoDetailScreen
import com.nhatvm.toptop.data.video.VideoDetailViewModel

@UnstableApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onShowComment: (Int) -> Unit
) {

    VerticalPager(pageCount = 10) { videoId ->
        val vieModel: VideoDetailViewModel = hiltViewModel(key = videoId.toString())
        VideoDetailScreen(videoId = videoId, vieModel = vieModel, onShowComment = onShowComment)
    }
}