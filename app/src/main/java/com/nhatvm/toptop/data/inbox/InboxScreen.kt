package com.nhatvm.toptop.data.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhatvm.toptop.data.Routes
import com.nhatvm.toptop.data.auth.repositories.User
import com.nhatvm.toptop.data.components.CircleImage
import com.nhatvm.toptop.data.components.TextBold
import com.nhatvm.toptop.data.video.repository.VideoRepository

@Composable
fun InboxScreen(USERCURRENT: User, openChat:(String, String) -> Unit, openAiChat: () -> Unit) {
    var inboxList by remember {
        mutableStateOf<List<User>>(emptyList())
    }
    LaunchedEffect(Unit){
        inboxList = VideoRepository().getInbox(USERCURRENT.id)
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ){
            TextBold(username = "Inbox", color = Color.Black, fontsize = 20.sp)
        }
        LazyColumn(){
            item(1){
                InboxItem(user = User("", "TopTop Ai", "", "", Routes.AVT_AI)) {
                    openAiChat()
                }
            }
            items(inboxList){
                InboxItem(it, openChat = {openChat(it.id, it.Phone)})
            }
        }

    }
}

@Composable
fun InboxItem(user: User, openChat:() -> Unit) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                openChat()
            }
    ){
        CircleImage(
            imageUrl = user.Image,
            size = 55.dp
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column {
            TextBold(username = "${user.Name}", color = Color.Black, fontsize = 18.sp)
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "đã gửi tin nhắn cho bạn", fontSize = 16.sp)
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = "- 5 giờ", color = Color.LightGray)
            }
        }
    }
}