package com.nhatvm.toptop.data.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.auth.repositories.User
import com.nhatvm.toptop.data.components.CircleImage
import com.nhatvm.toptop.data.components.TextBold
import com.nhatvm.toptop.data.theme.lightgray
import com.nhatvm.toptop.data.theme.lightred
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun DiscoverScreen() {
    val fireDatabase = FirebaseDatabase.getInstance()
    val userRef = fireDatabase.getReference("users")
    val coroutineScope = rememberCoroutineScope()
    var input by remember {
        mutableStateOf("")
    }
    var users by remember {
        mutableStateOf<List<User>>(emptyList())
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            TextField(
                value = input,
                onValueChange = { input = it },
                leadingIcon = { Icon(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = ""
                )},
                placeholder = { Text(text = "Nhập userId...") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = lightgray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,

                    ),
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .weight(1f)
            )
            TextButton( onClick = {
                if (input.isEmpty()){

                }else{
                    coroutineScope.launch {
                        val usersList = mutableListOf<User>()
                        val snapshot = userRef.get().await()
                        if (snapshot.exists()) {
                            for (child in snapshot.children) {
                                val username = child.child("username").getValue(String::class.java) ?: ""
                                if (username.contains(input)) {
                                    val name = child.child("name").getValue(String::class.java) ?: ""
                                    val phone = child.child("phone").getValue(String::class.java) ?: ""
                                    val image = child.child("image").getValue(String::class.java) ?: ""
                                    usersList.add(User("", name, phone, username, image))
                                }
                            }
                        }
                        users = usersList
                    }
                }
            } ) {
                Text(
                    text ="Tìm kiếm",
                    color = lightred,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ){
            if (users.isEmpty()){
                TextBold(username = "Kết nối với nhau !", color = Color.Black, fontsize = 18.sp)
            }
            LazyColumn(){
                items(users){
                    UserItem(it)
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ){
        CircleImage(
            imageUrl = user.Image,
            size = 55.dp
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column (modifier = Modifier.weight(1f)){
            TextBold(username = "${user.Name}", color = Color.Black, fontsize = 18.sp)
            Text(text = "${user.Username}")
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "${user.Phone}", color = Color.Gray)
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = lightred
            ),
            modifier = Modifier.width(120.dp)
        ) {
            Text(
                text = "Inbox",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}