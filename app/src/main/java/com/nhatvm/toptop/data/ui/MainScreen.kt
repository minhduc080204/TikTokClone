package com.nhatvm.toptop.data.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
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
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nhatvm.toptop.data.Routes
import com.nhatvm.toptop.data.auth.SignInScreen
import com.nhatvm.toptop.data.auth.SignUpScreen
import com.nhatvm.toptop.data.auth.repositories.User
import com.nhatvm.toptop.data.components.Header
import com.nhatvm.toptop.data.components.TabBottomBar
import com.nhatvm.toptop.data.discover.DiscoverScreen
import com.nhatvm.toptop.data.file.openGalleryForVideo
import com.nhatvm.toptop.data.file.uploadVideotoTopTop
import com.nhatvm.toptop.data.foryou.ForYouScreen
import com.nhatvm.toptop.data.inbox.AIChatScreen
import com.nhatvm.toptop.data.inbox.ChatScreen
import com.nhatvm.toptop.data.inbox.InboxScreen
import com.nhatvm.toptop.data.uploadvideo.UploadVideoScreen
import com.nhatvm.toptop.data.user.FollowingScreen
import com.nhatvm.toptop.data.user.ProfileScreen
import com.nhatvm.toptop.data.user.UpdateProfileScreen
import com.nhatvm.toptop.data.video.repository.Video
import com.nhatvm.toptop.data.video.repository.VideoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Route

@UnstableApi
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen() {
    lateinit var USERCURRENT:User
    lateinit var userId:String
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var fireDatabase: FirebaseDatabase
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState(initialPage = 1)
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val videoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { videoUri ->
                navController.navigate("UPLOAD_SCREEN/${Uri.encode(videoUri.toString())}"){
                    popUpTo(Routes.FORYOU_SCREEN){
                        inclusive = true
                    }
                }
            }
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openGalleryForVideo(videoPickerLauncher)
        }
    }
    var listVideoInfor by remember { mutableStateOf<List<Video>>(listOf()) }
    val context = LocalContext.current
    var selectItem by remember {
        mutableStateOf(Routes.FORYOU_SCREEN)
    }
    var isShowBottomTab by remember {
        mutableStateOf(false)
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
            2 -> selectItem = Routes.PROFILE_SCREEN
            else -> selectItem = Routes.FORYOU_SCREEN
        }
    }
    val onHomeClick:()-> Unit = {
        navController.navigate(Routes.FORYOU_SCREEN){
            popUpTo(Routes.FORYOU_SCREEN){
                inclusive = true
            }
        }
        isShowBottomTab = true
        selectItem = Routes.FORYOU_SCREEN
    }
    val onDiscoverClick:()-> Unit = {
        navController.navigate(Routes.DISCOVER_SCREEN){
            popUpTo(Routes.FORYOU_SCREEN){
                inclusive = true
            }
        }
        isShowBottomTab = true
        selectItem = Routes.DISCOVER_SCREEN
    }
    val onInboxClick:()-> Unit = {
        navController.navigate(Routes.INBOX_SCREEN){
            popUpTo(Routes.FORYOU_SCREEN){
                inclusive = true
            }
        }
        isShowBottomTab = true
        selectItem = Routes.INBOX_SCREEN
    }
    val onProfileClick:()-> Unit = {
        navController.navigate(Routes.PROFILE_SCREEN){
            popUpTo(Routes.FORYOU_SCREEN){
                inclusive = true
            }
        }
        isShowBottomTab = true
        selectItem = Routes.PROFILE_SCREEN
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            toggleHeader(page)
        }
    }
    Scaffold (
        bottomBar = {
            if (isShowBottomTab){
                TabBottomBar(
                    selectItem,
                    onHomeClick = {
                        onHomeClick()
                    },
                    onSearchClick = {
                        onDiscoverClick()
                    },
                    onUploadClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        } else {
                            openGalleryForVideo(videoPickerLauncher)
                        }
                    },
                    onInboxClick = {
                        onInboxClick()
                    },
                    onProfileClick = {
                        onProfileClick()
                    },
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            NavHost(navController = navController, startDestination = Routes.SIGNIN_SCREEN){
                composable(Routes.SIGNIN_SCREEN){
                    var isLoading by remember { mutableStateOf(false) }
                    SignInScreen(
                        onSignIn = {email, pass ->
                            if(email.isEmpty() || pass.isEmpty()){
                                Toast.makeText(context, "Hãy nhập Email và mật khẩu", Toast.LENGTH_SHORT).show()
                            }else{
                                firebaseAuth = FirebaseAuth.getInstance()
                                firebaseAuth.signInWithEmailAndPassword(email.trim(), pass.trim())
                                    .addOnCompleteListener{
                                        if (it.isSuccessful){
                                            isLoading = true
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
                                                        isShowBottomTab = true
                                                        scrollToPage(1)
                                                    }
                                                )
                                            }
                                        }else{
                                            Toast.makeText(context, "Sai emai hoặc mật khẩu !", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, it.message ?: "Đã xảy ra lỗi không xác định khi thêm vào cơ sở dữ liệu", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        },
                        onSignUp = {
                            navController.navigate(Routes.SIGNUP_SCREEN)
                        },
                        isLoading = isLoading
                    )
                }
                composable(Routes.SIGNUP_SCREEN){
                    SignUpScreen(
                        onSignUp = {name,phone,username,email, password->
                            if(email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || username.isEmpty()){
                                Toast.makeText(context, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                            }else {
                                firebaseAuth = FirebaseAuth.getInstance()
                                firebaseAuth.createUserWithEmailAndPassword(email.trim(), password.trim())
                                    .addOnCompleteListener{
                                        if (it.isSuccessful){
                                            userId = firebaseAuth.currentUser?.uid.toString()
                                            val usersignup = User(userId, name, phone, "@$username", Routes.AVT_USER)
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
                                    .addOnFailureListener {
                                        Toast.makeText(context, it.message ?: "Đã xảy ra lỗi không xác định khi thêm vào cơ sở dữ liệu", Toast.LENGTH_SHORT).show()
                                    }

                            }
                        },
                        onSignIn = { navController.navigate(Routes.SIGNIN_SCREEN) }
                    )
                }
                composable(Routes.FORYOU_SCREEN){
                    isShowBottomTab = true
                    LaunchedEffect(Unit) {
                        listVideoInfor = VideoRepository().getVideoObject()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ){
                        Column (
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        ){
                            HorizontalPager(pageCount = 3, state = pagerState) {page ->
                                when(page){
                                    1 -> ForYouScreen(
                                        USERCURRENT = USERCURRENT,
                                        context = context,
                                        listVideoInfor = listVideoInfor
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
                composable(Routes.DISCOVER_SCREEN){
                    DiscoverScreen(
                        myId = USERCURRENT.id
                    ){
                        navController.navigate(Routes.INBOX_SCREEN)
                    }
                }
                composable(Routes.INBOX_SCREEN){
                    isShowBottomTab = true
                    InboxScreen(
                        USERCURRENT = USERCURRENT,
                        openChat = {id, phone ->
                            navController.navigate("MESSAGE_SCREEN/$id/$phone")
                        },
                        openAiChat = {
                            navController.navigate(Routes.AICHAT_SCREEN)
                        }
                    )
                }
                composable(
                    Routes.MESSAGE_SCREEN,
                    arguments = listOf(
                        navArgument("userid"){ type = NavType.StringType },
                        navArgument("messageId"){ type = NavType.StringType }
                    )
                ){
                    isShowBottomTab = false
                    val userIdString = it.arguments?.getString("userid")?:""
                    val messageId = it.arguments?.getString("messageId")?:""
                    ChatScreen(
                        userId = userIdString,
                        messageId = messageId,
                        USERCURRENT = USERCURRENT,
                        onBack = {
                            onInboxClick()
                        },
                        sendMessage = {
                            keyboardController?.hide()
                            VideoRepository().sendMessage(messageId, it, USERCURRENT.id)
                        }
                    )
                }
                composable(Routes.AICHAT_SCREEN){
                    isShowBottomTab = false
                    AIChatScreen(
                        onBack = {
                            onInboxClick()
                        },
                    )
                }
                composable(Routes.PROFILE_SCREEN){
                    ProfileScreen(
                        user = USERCURRENT,
                        onLognOut = {
                            isShowBottomTab = false
                            navController.navigate(Routes.SIGNIN_SCREEN){
                                popUpTo(Routes.FORYOU_SCREEN){
                                    inclusive = true
                                }
                            }
                        },
                        onUpdateProfile = {
                            isShowBottomTab = false
                            navController.navigate(Routes.UPDATEPROFILE_SCREEN)
                        }
                    )
                }
                composable(Routes.UPDATEPROFILE_SCREEN){
                    UpdateProfileScreen(
                        user = USERCURRENT,
                        onBack = { onProfileClick() },
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
                    isShowBottomTab = false
                    val videoUri = Uri.parse(it.arguments?.getString("videoUri"))
                    var isLoading by remember { mutableStateOf(false) }
                    UploadVideoScreen(
                        isLoading = isLoading,
                        videoUri = videoUri,
                        onUpload = {content, listHashtag, song ->
                            coroutineScope.launch {
                                isLoading = true
                                val urlVideo = uploadVideotoTopTop(videoUri) ?: ""
                                VideoRepository().uploadVideo(
                                    idVideo = USERCURRENT.id,
                                    contentVideo = content,
                                    songVideo = song,
                                    tagsVideo = listHashtag,
                                    urlVideo = urlVideo,
                                )
                                delay(10L)
                                onHomeClick()
                                isLoading = false
                                Toast.makeText(context, "Đăng tải video thành công", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onBack = {
                            onHomeClick()
                        },
                        onChose = {
                            openGalleryForVideo(videoPickerLauncher)
                        }
                    )
                }
            }
        }
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnected == true
}