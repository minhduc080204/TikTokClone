package com.nhatvm.toptop.data

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.nhatvm.toptop.data.theme.TopTopTheme
import com.nhatvm.toptop.data.ui.MainScreen
import com.nhatvm.toptop.data.video.repository.VideoRepository
import dagger.hilt.android.AndroidEntryPoint


@UnstableApi
@AndroidEntryPoint
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TopTopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
//                    Player()
//                    LaunchedEffect(Unit) {
//                        for (video in VideoRepository().getVideoObject()) {
//                            Log.d("CCCC", "Video URL: ${video.urlVideo}")
//                            Log.d("CCCC", "Username Video: ${video.usernameVideo}")
//                            Log.d("CCCC", "Content Video: ${video.contentVideo}")
//                            Log.d("CCCC", "Tags Video: ${video.tagsVideo.joinToString(", ")}")
//                            Log.d("CCCC", "Song Video: ${video.songVideo}")
//                            Log.d("CCCC", "Like Video: ${video.likeVideo}")
//                            Log.d("CCCC", "Share Video: ${video.shareVideo}")
//
//                            Log.d("CCCC", "User Video ID: ${video.userVideo.id}")
//                            Log.d("CCCC", "User Video Name: ${video.userVideo.Name}")
//                            Log.d("CCCC", "User Video Phone: ${video.userVideo.Phone}")
//                            Log.d("CCCC", "User Video Username: ${video.userVideo.Username}")
//
//                            for (comment in video.commentVideo) {
//                                Log.d("CCCC", "Comment: ${comment.comment}")
//                                Log.d("CCCC", "Like Comment: ${comment.likeComment}")
//                                Log.d("CCCC", "Time Comment: ${comment.timeComment}")
//
//                                Log.d("CCCC", "User Comment ID: ${comment.userComment.id}")
//                                Log.d("CCCC", "User Comment Name: ${comment.userComment.Name}")
//                                Log.d("CCCC", "User Comment Phone: ${comment.userComment.Phone}")
//                                Log.d("CCCC", "User Comment Username: ${comment.userComment.Username}")
//                            }
//                        }
//                    }


                }
            }
        }
    }
}