package com.nhatvm.toptop.data.ui

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nhatvm.toptop.data.Routes
import com.nhatvm.toptop.data.auth.SignInScreen
import com.nhatvm.toptop.data.auth.SignUpScreen
import com.nhatvm.toptop.data.auth.repositories.User
import com.nhatvm.toptop.data.components.CommentScreen
import com.nhatvm.toptop.data.components.Header
import com.nhatvm.toptop.data.components.ShareBar
import com.nhatvm.toptop.data.components.TabBottomBar
import com.nhatvm.toptop.data.file.FileViewModel
import com.nhatvm.toptop.data.file.handleVideoUri
import com.nhatvm.toptop.data.file.handleVideoUri1
import com.nhatvm.toptop.data.file.openGalleryForVideo
import com.nhatvm.toptop.data.foryou.ForYouScreen
import com.nhatvm.toptop.data.inbox.InboxScreen
import com.nhatvm.toptop.data.uploadvideo.UploadVideoScreen
import com.nhatvm.toptop.data.user.FollowingScreen
import com.nhatvm.toptop.data.user.ProfileScreen
import com.nhatvm.toptop.data.user.UpdateProfileScreen
import com.nhatvm.toptop.data.video.repository.Comment
import com.nhatvm.toptop.data.video.repository.Video
import com.nhatvm.toptop.data.video.repository.VideoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@UnstableApi
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,ExperimentalComposeUiApi::class)
@Composable
fun MainScreen() {
    lateinit var USERCURRENT:User
    lateinit var userId:String
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var fireDatabase: FirebaseDatabase
    val pagerState = rememberPagerState(initialPage = 1)
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val videoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { videoUri ->
                navController.navigate("UPLOAD_SCREEN/${Uri.encode(videoUri.toString())}")
            }
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openGalleryForVideo(videoPickerLauncher)
        } else {

        }
    }
    val context = LocalContext.current
    var selectItem by remember {
        mutableStateOf(Routes.FORYOU_SCREEN)
    }
    var isShowHeader by remember {
        mutableStateOf(true)
    }
    val toggleHeader = { page: Int ->
        if (page >=2) {
            isShowHeader = false
        }else{
            isShowHeader = true
        }
    }
    val scrollToPage: (Int) -> Unit = { page ->
        coroutineScope.launch {
            delay(10L)
            pagerState.scrollToPage(page = page)
        }
        when(page){
            2 -> selectItem = Routes.ME_SCREEN
            3 -> selectItem = Routes.INBOX_SCREEN
            else -> selectItem = Routes.FORYOU_SCREEN
        }
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            toggleHeader(page)
        }
    }
    NavHost(navController = navController, startDestination = Routes.SIGNIN_SCREEN){
        composable(Routes.SIGNIN_SCREEN){
            SignInScreen(
                onSignIn = {email, pass ->
                    if(email.isEmpty() || pass.isEmpty()){
                        Toast.makeText(context, "Hãy nhập Email và mật khẩu", Toast.LENGTH_SHORT).show()
                    }else{
                        firebaseAuth = FirebaseAuth.getInstance()
                        firebaseAuth.signInWithEmailAndPassword(email.trim(), pass.trim()).addOnCompleteListener{
                            if (it.isSuccessful){
                                userId = firebaseAuth.currentUser?.uid.toString()
                                coroutineScope.launch {
                                    USERCURRENT = VideoRepository().getUserById(
                                        userId = userId,
                                        onSuccess = {
                                            navController.navigate(Routes.FORYOU_SCREEN){
                                                popUpTo(Routes.SIGNIN_SCREEN){
                                                    inclusive = true
                                                }
                                            }
                                            scrollToPage(1)
                                        }
                                    )
                                }
                            }else{
                                Toast.makeText(context, "Sai emai hoặc mật khẩu !", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                onSignUp = {
                    navController.navigate(Routes.SIGNUP_SCREEN)
                }
            )
        }
        composable(Routes.SIGNUP_SCREEN){
            SignUpScreen(
                onSignUp = {name,phone,username,email, password->
                    if(email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || username.isEmpty()){
                        Toast.makeText(context, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    }else {
                        firebaseAuth = FirebaseAuth.getInstance()
                        firebaseAuth.createUserWithEmailAndPassword(email.trim(), password.trim()).addOnCompleteListener{
                            if (it.isSuccessful){
                                userId = firebaseAuth.currentUser?.uid.toString()
                                 val usersignup = User(userId, name, phone, "@$username", "")
                                fireDatabase = FirebaseDatabase.getInstance()
                                val users: DatabaseReference = fireDatabase.getReference("users")
                                users.child(userId).setValue(usersignup).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        navController.navigate(Routes.SIGNIN_SCREEN)
                                        Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }else{
                                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                onSignIn = { navController.navigate(Routes.SIGNIN_SCREEN) }
            )
        }
        composable(Routes.FORYOU_SCREEN){
            Scaffold (
                bottomBar = { TabBottomBar(
                    selectItem,
                    onHomeClick = {
                        scrollToPage(1)
                    },
                    onSearchClick = {

                    },
                    onUploadClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        } else {
                            openGalleryForVideo(videoPickerLauncher)
                        }
                    },
                    onInboxClick = {
                        scrollToPage(3)
                    },
                    onProfileClick = {
                        scrollToPage(2)
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
                        HorizontalPager(pageCount = 4, state = pagerState) {page ->
                            when(page){
                                1 -> ForYouScreen(
                                    USERCURRENT = USERCURRENT,
                                    context = context,
                                )
                                2 -> ProfileScreen(
                                    user = USERCURRENT,
                                    onLognOut = {
                                        navController.navigate(Routes.SIGNIN_SCREEN){
                                            popUpTo(Routes.FORYOU_SCREEN){
                                                inclusive = true
                                            }
                                        }
                                    },
                                    onUpdateProfile = {
                                        navController.navigate(Routes.UPDATEPROFILE_SCREEN)
                                    }
                                )
                                3 -> InboxScreen()
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
                                    onFollowingTab = {
                                        scrollToPage(0)
                                    },
                                    onForyouTab = {
                                        scrollToPage(1)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        composable(Routes.UPDATEPROFILE_SCREEN){
            UpdateProfileScreen(
                user = USERCURRENT,
                onBack = {
                    navController.navigate(Routes.FORYOU_SCREEN)
                    coroutineScope.launch {
                        delay(10L)
                        scrollToPage(2)
                    }

                },
                onUpdate = {name, phone, username ->
                    if(name.trim().isEmpty() || phone.trim().isEmpty() || username.trim().isEmpty()) {
                        Toast.makeText(context, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    }else{
                        if (phone.toIntOrNull() == null){
                            Toast.makeText(context, "Hãy nhập đúng số điện thoại", Toast.LENGTH_SHORT).show()
                        }else{
                            val newUser = User(userId, name, phone, username, USERCURRENT.Image)
                            if (!newUser.equals(USERCURRENT)){
                                USERCURRENT = newUser
                                fireDatabase = FirebaseDatabase.getInstance()
                                val userRef: DatabaseReference = fireDatabase.getReference("users").child(userId)
                                userRef.setValue(USERCURRENT).addOnCompleteListener{it ->
                                    if(it.isSuccessful){
                                        navController.navigate(Routes.FORYOU_SCREEN)
                                        coroutineScope.launch {
                                            delay(10L)
                                            scrollToPage(2)
                                        }
                                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
        composable(Routes.UPLOAD_SCREEN){
            val videoUri = Uri.parse(it.arguments?.getString("videoUri"))
            var isLoading by remember { mutableStateOf(false) }
            UploadVideoScreen(
                isLoading = isLoading,
                videoUri = videoUri,
                onUpload = {content, listHashtag, song ->
                    coroutineScope.launch {
                        isLoading = true
                        val urlVideo = handleVideoUri1(videoUri)?: ""
                        VideoRepository().uploadVideo(
                            idVideo = USERCURRENT.id,
                            contentVideo = content,
                            songVideo = song,
                            tagsVideo = listHashtag,
                            urlVideo = urlVideo,
                        )
                        delay(10L)
                        navController.navigate(Routes.FORYOU_SCREEN)
                        isLoading = false
                        Toast.makeText(context, "Đăng tải video thành công", Toast.LENGTH_SHORT).show()
                    }
                },
                onBack = {
                    navController.navigate(Routes.FORYOU_SCREEN)
                },
                onChose = {
                    openGalleryForVideo(videoPickerLauncher)
                }
            )
        }
    }
}