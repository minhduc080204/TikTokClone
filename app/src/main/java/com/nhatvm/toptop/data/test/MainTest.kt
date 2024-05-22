package com.nhatvm.toptop.data.test

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun ExoPlayerVideo(url: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val exoPlayer = remember { SimpleExoPlayer.Builder(context).build() }

    LaunchedEffect(url) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

        // Observe lifecycle events to manage player properly
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                Lifecycle.Event.ON_DESTROY -> exoPlayer.release()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

    }

    Surface(modifier = Modifier.fillMaxSize()) {
        PlayerView(context).apply {
            this.player = exoPlayer
        }
    }
}


@Composable
fun MainTesst() {
    lateinit var exoPlayer: SimpleExoPlayer
    val videos = ArrayList<Video>()
    val exoPlayerItems = ArrayList<ExoPlayerItem>()
    videos.add(
        Video(
            "Big Buck Bunny",
            "https://firebasestorage.googleapis.com/v0/b/toptopclone-b61e4.appspot.com/o/video%2FDownload.mp4?alt=media&token=f1e201f1-7801-4563-9ddf-ce41c186acb9"
        )
    )

    videos.add(
        Video(
            "Elephant Dream",
            "https://firebasestorage.googleapis.com/v0/b/toptopclone-b61e4.appspot.com/o/video%2Fvideo1.mp4?alt=media&token=bca76ac3-59fa-4ca7-bde4-59c671b08751"
        )
    )

    videos.add(
        Video(
            "For Bigger Blazes",
            "https://firebasestorage.googleapis.com/v0/b/toptopclone-b61e4.appspot.com/o/video%2Fvideo2.mp4?alt=media&token=78ff139b-1bdd-41ed-8a5d-4183b4b342e8"
        )
    )
    ExoPlayerVideo("https://firebasestorage.googleapis.com/v0/b/toptopclone-b61e4.appspot.com/o/video%2Fvideo2.mp4?alt=media&token=78ff139b-1bdd-41ed-8a5d-4183b4b342e8")
}