package com.nhatvm.toptop.data.video

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.components.VideoInterface
import com.nhatvm.toptop.data.designsystem.TopTopVideoPlayer
import com.nhatvm.toptop.data.video.repository.Video

@UnstableApi
@Composable
fun VideoDetailScreen(
    videoId: Int,
    videoinfor: Video,
    viewModel: VideoDetailViewModel = hiltViewModel(),
    onShowComment: (Int) -> Unit,
    onShowShare: (Int) -> Unit,
    isPlaying: Boolean
) {

    val uiState = viewModel.uiState.collectAsState()

    if (uiState.value == VideoDetailUiState.Default) {
        // loading
        viewModel.handleAction(VideoDetailAction.LoadData(videoId))
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            viewModel.playVideo()
        } else {
            viewModel.pauseVideo()
        }
    }

    VideoDetailScreen(
        videoinfor = videoinfor,
        uiState = uiState.value,
        player = viewModel.videoPlayer,
        onShowComment = {onShowComment(videoId)},
        onShowShare = { onShowShare(videoId)}
    ) { aciton ->
        viewModel.handleAction(action = aciton)
    }
}

@UnstableApi
@Composable
fun VideoDetailScreen(
    uiState: VideoDetailUiState,
    videoinfor: Video,
    player: Player,
    onShowComment: () -> Unit,
    onShowShare: () -> Unit,
    handleAction: (VideoDetailAction) -> Unit
) {
    when (uiState) {
        is VideoDetailUiState.Loading -> {
            val infiniteTransition = rememberInfiniteTransition()

            val rotate by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = InfiniteRepeatableSpec(
                    repeatMode = RepeatMode.Restart,
                    animation = tween(
                        durationMillis = 2000,
                        easing = LinearEasing
                    )
                )
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.loading_icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(60.dp)
                        .rotate(rotate)
                )
            }
        }

        is VideoDetailUiState.Success -> {
            VideoDetailScreen(
                videoinfor = videoinfor,
                player = player,
                handleAction = handleAction,
                onShowComment = onShowComment,
                onShowShare = onShowShare
            )
        }

        else -> {

        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@UnstableApi
@Composable
fun VideoDetailScreen(
    player: Player,
    videoinfor: Video,
    handleAction: (VideoDetailAction) -> Unit,
    onShowComment: () -> Unit,
    onShowShare: () -> Unit,
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .clickable(
            onClick = {
                handleAction(VideoDetailAction.ToggleVideo)
            }
        )) {
        val (videoPlayerView) = createRefs()
        TopTopVideoPlayer(player = player, modifier = Modifier.constrainAs(videoPlayerView) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.matchParent
            height = Dimension.matchParent
        })
    }
    VideoInterface(
        videoinfor = videoinfor,
        onAvatarClick = {},
        onLikeClick = {},
        onCommentClick = onShowComment,
        onShareClick = onShowShare,
    )
}