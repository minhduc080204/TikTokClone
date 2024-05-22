package com.nhatvm.toptop.data.test

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun Player() {
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val context = LocalContext.current

    val mediaItem =
        MediaItem.fromUri(
            "https://firebasestorage.googleapis.com/v0/b/toptopclone-b61e4.appspot.com/o/video%2Fvideo1.mp4?alt=media&token=bca76ac3-59fa-4ca7-bde4-59c671b08751"
        )

    val mediaSource: MediaSource =
        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(mediaItem)


    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaSource(mediaSource)
            prepare()
            playWhenReady = true
        }
    }
    exoPlayer.seekTo(0)
    exoPlayer.repeatMode = androidx.media3.common.Player.REPEAT_MODE_ALL
//        exoPlayer.

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            exoPlayer.release()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
        ,
        factory = {
            PlayerView(context).also { playerView ->
                playerView.player = exoPlayer
            }
        },
        update = {
            when (lifecycle) {
                Lifecycle.Event.ON_RESUME -> {
                    it.onPause()
                    it.player?.pause()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    it.onResume()
                }

                else -> Unit
            }
        }
    )

}