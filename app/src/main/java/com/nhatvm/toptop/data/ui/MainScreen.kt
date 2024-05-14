package com.nhatvm.toptop.data.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import com.nhatvm.toptop.data.components.Header
import com.nhatvm.toptop.data.components.TabBar
import com.nhatvm.toptop.data.foryou.ForYouScreen
import com.nhatvm.toptop.data.user.FollowingScreen
import com.nhatvm.toptop.data.user.ProfileScreen
import kotlinx.coroutines.launch

@UnstableApi
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
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
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ){
                Column(
                    modifier = Modifier
                        .background(Color.White)
                ){
                    Box(
                        modifier = Modifier
                            .height(LocalConfiguration.current.screenHeightDp.dp - 60.dp)
                    ){
                        Column (
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        ){
                            HorizontalPager(pageCount = 3, state = pagerState) {page ->
                                when(page){
                                    1 -> ForYouScreen(onShowComment = {})
                                    2 -> ProfileScreen()
                                    else -> FollowingScreen()
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier.padding(top = 40.dp),
                ){
                    if (isShowHeader){
                        androidx.compose.animation.AnimatedVisibility(visible = isShowHeader) {
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
            Box(){
                TabBar(navController)
            }
        }
    }
}