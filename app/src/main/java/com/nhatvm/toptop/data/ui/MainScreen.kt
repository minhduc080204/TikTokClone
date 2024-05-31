package com.nhatvm.toptop.data.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nhatvm.toptop.data.Routes
import com.nhatvm.toptop.data.auth.SignInScreen
import com.nhatvm.toptop.data.auth.SignUpScreen
import com.nhatvm.toptop.data.auth.repositories.User
import com.nhatvm.toptop.data.components.CommentScreen
import com.nhatvm.toptop.data.components.Header
import com.nhatvm.toptop.data.components.ShareBar
import com.nhatvm.toptop.data.components.TabBottomBar
import com.nhatvm.toptop.data.file.FileRepository
import com.nhatvm.toptop.data.foryou.ForYouScreen
import com.nhatvm.toptop.data.user.FollowingScreen
import com.nhatvm.toptop.data.user.ProfileScreen
import com.nhatvm.toptop.data.user.UpdateProfileScreen
import com.nhatvm.toptop.data.video.repository.VideoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@UnstableApi
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    lateinit var USERCURRENT:User
    lateinit var userId:String
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var fireDatabase: FirebaseDatabase
    val pagerState = rememberPagerState(initialPage = 1)
    val coroutineScope = rememberCoroutineScope()
    val fileRepository = FileRepository()
    val videoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { videoUri ->
                coroutineScope.launch {
                    fileRepository.handleVideoUri(videoUri)
                }
            }
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            fileRepository.openGalleryForVideo(videoPickerLauncher)
        } else {

        }
    }
    val context = LocalContext.current
    val navController = rememberNavController()
    var currentVideoId by remember {
        mutableStateOf(-1)
    }
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
            pagerState.scrollToPage(page = page)
        }
        if (page == 2) {
            selectItem = Routes.ME_SCREEN
        }else{
            selectItem = Routes.FORYOU_SCREEN
        }
        Log.d("DDDD", page.toString())
    }
    val content: @Composable (() -> Unit) = {  }
    var sheetContents by remember {
        mutableStateOf(content)
    }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val showsheetState: (Int) -> Unit = { videoId ->
        currentVideoId = videoId
        coroutineScope.launch {
            sheetState.show()
        }
    }
    val hidesheetState: () -> Unit = {
        currentVideoId = -1
        coroutineScope.launch {
            sheetState.hide()
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
                                 val usersignup = User(userId, name, phone, "@$username")
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
            ModalBottomSheetLayout(sheetState = sheetState, sheetContent = {
                if (currentVideoId != -1) {
                    sheetContents()
                } else {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }) {
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
                                fileRepository.openGalleryForVideo(videoPickerLauncher)
                            }
                        },
                        onInboxClick = {

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
                            HorizontalPager(pageCount = 3, state = pagerState) {page ->
                                when(page){
                                    1 -> ForYouScreen(
                                        onShowComment = {videoId ->
                                            sheetContents = {
                                                CommentScreen(
                                                    videoId = currentVideoId,
                                                    onComment = {content ->

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
                            USERCURRENT = User(userId, name, phone, username)
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
            )
        }
    }
}