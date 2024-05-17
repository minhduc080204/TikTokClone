package com.nhatvm.toptop.data.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import com.nhatvm.toptop.data.Screens.HomeScreen
import com.nhatvm.toptop.data.components.CommentScreen
import com.nhatvm.toptop.data.components.Header
import com.nhatvm.toptop.data.components.TabBottomBar
import com.nhatvm.toptop.data.foryou.ForYouScreen
import com.nhatvm.toptop.data.user.FollowingScreen
import com.nhatvm.toptop.data.user.ProfileScreen
import kotlinx.coroutines.launch

@UnstableApi
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var currentVideoId by remember {
        mutableStateOf(-1)
    }
    val pagerState = rememberPagerState(initialPage = 1)
    val coroutineScope = rememberCoroutineScope()
    val scrollToPage: (Boolean) -> Unit = { isForU ->
        val page = if (isForU) 1 else 0
        coroutineScope.launch {
            pagerState.scrollToPage(page = page)
        }
    }
    var isShowHeader by remember {
        mutableStateOf(true)
    }
    val toggleHeader = { isShow: Boolean ->
        if (isShowHeader != isShow) {
            isShowHeader = isShow
        }
    }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val showCommentScreen: (Int) -> Unit = { videoId ->
        currentVideoId = videoId
        coroutineScope.launch {
            sheetState.show()
        }
    }
    val hideCommentScreen: () -> Unit = {
        currentVideoId = -1
        coroutineScope.launch {
            sheetState.hide()
        }
    }
    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page == 2) {
                // hide tab content
                toggleHeader(false)
            } else {
                // show tab content
                toggleHeader(true)
            }
        }
    }
    ModalBottomSheetLayout(sheetState = sheetState, sheetContent = {
        if (currentVideoId != -1) {
            CommentScreen(videoId = currentVideoId) {
                hideCommentScreen()
            }
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }) {
        Scaffold (
            bottomBar = { TabBottomBar(
                onHomeClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(page = 1)
                    }
                },
                onSearchClick = {

                },
                onUploadClick = {

                },
                onInboxClick = {

                },
                onProfileClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(page = 2)
                    }
                },
            ) }
        ){paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ){
                Column (
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ){
                    HorizontalPager(pageCount = 3, state = pagerState) {page ->
                        when(page){
                            1 -> HomeScreen(){videoId ->
                                showCommentScreen(videoId)
                            }
                            2 -> ProfileScreen()
                            else -> FollowingScreen()
                        }
                    }
                }
                Box(
                    modifier = Modifier.padding(top = 40.dp),
                ){
                    if (isShowHeader){
                        AnimatedVisibility(visible = isShowHeader) {
                            Header(
                                isTabSelectedIndex = pagerState.currentPage,
                                onSelectedTab = {isForU ->
                                    scrollToPage(isForU)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}