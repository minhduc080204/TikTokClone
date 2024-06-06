package com.nhatvm.toptop.data.foryou

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.google.firebase.database.FirebaseDatabase
import com.nhatvm.toptop.data.auth.repositories.User
import com.nhatvm.toptop.data.components.CommentScreen
import com.nhatvm.toptop.data.components.ShareBar
import com.nhatvm.toptop.data.video.VideoDetailScreen
import com.nhatvm.toptop.data.video.VideoDetailViewModel
import com.nhatvm.toptop.data.video.repository.Comment
import com.nhatvm.toptop.data.video.repository.Video
import com.nhatvm.toptop.data.video.repository.VideoRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@UnstableApi
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,ExperimentalComposeUiApi::class)
@Composable
fun ForYouScreen(
    USERCURRENT: User,
    context: Context,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    lateinit var fireDatabase: FirebaseDatabase
    var currentVideoId by remember {
        mutableStateOf(-1)
    }
    val pagerState = rememberPagerState()
    var listVideoInfor by remember { mutableStateOf<List<Video>>(listOf()) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        listVideoInfor = VideoRepository().getVideoObject()
        pagerState.scrollToPage(page = 0)
    }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val content: @Composable (() -> Unit) = {  }
    var sheetContents by remember {
        mutableStateOf(content)
    }

    val showsheetState: (Int) -> Unit = { videoId ->
        currentVideoId = videoId
        coroutineScope.launch {
            sheetState.show()
        }
    }
    val hidesheetState: () -> Unit = {
        coroutineScope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(sheetState = sheetState, sheetContent = {
        if (currentVideoId != -1) {
            sheetContents()
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }) {
        VerticalPager(state = pagerState, pageCount = listVideoInfor.size) { videoId ->
            var comments by remember (videoId){
                mutableStateOf<List<Comment>>(listOf())
            }
            currentVideoId = videoId
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
                onShowComment = {videoId ->
                    coroutineScope.launch {
                        comments = VideoRepository().getVideoObject()[videoId].commentVideo
                    }
                    sheetContents = {
                        CommentScreen(
                            usrer = USERCURRENT,
                            comments = comments,
                            onComment = {content ->
                                if (content.trim().isEmpty()){
                                    Toast.makeText(context, "Hãy nhập bình luận !", Toast.LENGTH_SHORT).show()
                                }else{
                                    val newComment = hashMapOf(
                                        "userId" to USERCURRENT.id,
                                        "comment" to content,
                                        "likeComment" to 0,
                                        "timeComment" to System.currentTimeMillis()
                                    )
                                    fireDatabase = FirebaseDatabase.getInstance()
                                    coroutineScope.launch {
                                        val videosRef = fireDatabase.getReference("videos")
                                        val commentKey = videosRef.get().await().children.toList()[videoId].key?: ""
                                        val newCommentRef = videosRef.child(commentKey).child("commentVideo")
                                        newCommentRef.push().setValue(newComment)
                                            .addOnSuccessListener {
                                                coroutineScope.launch {
                                                    comments = VideoRepository().getVideoObject()[videoId].commentVideo
                                                }
                                                keyboardController?.hide()
                                                Toast.makeText(context, "Bình luận thành công !", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(context, "Thất bại. Hãy thử lại !", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                            }
                        )
                    }
                    showsheetState(videoId)
                },
                onShowShare = {videoId ->
                    sheetContents = {
                        ShareBar(videoId = currentVideoId){
                            hidesheetState()
                        }
                    }
                    showsheetState(videoId)
                },
                isPlaying = isplay,
            )
        }
    }

}