package com.nhatvm.toptop.data.video

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.nhatvm.toptop.data.components.VideoInterface
import com.nhatvm.toptop.data.designsystem.TopTopVideoPlayer

@UnstableApi
@Composable
fun VideoDetailScreen(
    videoId: Int,
    vieModel: VideoDetailViewModel = hiltViewModel(),
    onShowComment: (Int) -> Unit
) {

    val uiState = vieModel.uiState.collectAsState()

    if (uiState.value == VideoDetailUiState.Default) {
        // loading
        vieModel.handleAction(VideoDetailAction.LoadData(videoId))
    }

    VideoDetailScreen(uiState = uiState.value, player = vieModel.videoPlayer, onShowComment = {
        onShowComment(videoId)
    }) { aciton ->
        vieModel.handleAction(action = aciton)
    }
}

@UnstableApi
@Composable
fun VideoDetailScreen(
    uiState: VideoDetailUiState,
    player: Player,
    onShowComment: () -> Unit,
    handleAction: (VideoDetailAction) -> Unit
) {
    when (uiState) {
        is VideoDetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "loading ...")
            }
        }

        is VideoDetailUiState.Success -> {
            VideoDetailScreen(
                player = player,
                handleAction = handleAction,
                onShowComment = onShowComment,
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
    handleAction: (VideoDetailAction) -> Unit,
    onShowComment: () -> Unit
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .clickable(
            onClick = {
                handleAction(VideoDetailAction.ToggleVideo)
            }
        )) {
        val (videoPlayerView, sideBar, videoInfo) = createRefs()
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
        username = "@ductihong82",
        content = "Tao là bố của chúng mày",
        hastag = listOf("#calisthenic", "#top1vku"),
        songname = "Avicii - Waiting for love",
        onAvatarClick = {},
        onLikeClick = {},
        onCommentClick = onShowComment,
        onShareClick = {},
    )
}