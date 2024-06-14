package com.nhatvm.toptop.data.inbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.Routes
import com.nhatvm.toptop.data.auth.repositories.User
import com.nhatvm.toptop.data.components.CircleImage
import com.nhatvm.toptop.data.components.TextBold
import com.nhatvm.toptop.data.inbox.components.MyMessage
import com.nhatvm.toptop.data.inbox.components.YourMessage
import com.nhatvm.toptop.data.theme.lightblue
import com.nhatvm.toptop.data.theme.lightgray
import com.nhatvm.toptop.data.video.repository.Message
import com.nhatvm.toptop.data.video.repository.VideoRepository
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(userId: String, messageId: String, USERCURRENT: User, onBack:() -> Unit, sendMessage:(String) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var user by remember {
        mutableStateOf(User("", "", "", "", Routes.AVT_USER))
    }
    var messages by remember {
        mutableStateOf(listOf<Message>())
    }
    LaunchedEffect(Unit){
        user = VideoRepository().getUserById(userId, {})
        messages = VideoRepository().getMessage1(messageId)
    }
    var chatinput by remember {
        mutableStateOf("")
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(lightgray)
            .padding(10.dp)
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 5.dp)
        ){
            Image(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onBack()
                    }
            )
            CircleImage(imageUrl = user.Image, size = 45.dp)
            Spacer(modifier = Modifier.size(10.dp))
            TextBold(username = "${user.Name}", color = Color.Black, fontsize = 20.sp)
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
        LazyColumn (
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.weight(1f)
        ){
            items(messages){
                if (it.userId == USERCURRENT.id){
                    MyMessage(it.content)
                }else{
                    YourMessage(
                        urlImage = user.Image,
                        content = it.content
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(15.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .fillMaxWidth()
                .background(Color.White)
        ){
            TextField(
                value = chatinput,
                onValueChange = { chatinput = it },
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                placeholder = { Text(text = "Nhắn tin...", color = Color.LightGray, fontSize = 18.sp) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.sendcomment_icon),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        sendMessage(chatinput)
                        coroutineScope.launch {
                            messages = VideoRepository().getMessage1(messageId)
                        }
                        chatinput = ""
                    }
            )
        }

    }
}