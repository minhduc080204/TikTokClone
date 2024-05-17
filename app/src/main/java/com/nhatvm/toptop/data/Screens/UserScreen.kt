package com.nhatvm.toptop.data.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.components.CircleImage
import com.nhatvm.toptop.data.components.LineColor
import com.nhatvm.toptop.data.components.TabBottomBar

@Composable
fun UserScreen(navController: NavController){
    Column (

    ){
        Column (
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
                .fillMaxSize()
        ){
            TopBar()
            User()
            UserVideo()
        }
        Box {
        }
    }
}

@Composable
fun TextBold(text:String, mordifier:Modifier = Modifier){
    Text(
        text = text,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = mordifier
    )
}

@Composable
fun TopBar(){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.addaccount_icon),
            contentDescription = "add"
        )
        TextBold(text = "Minh Đức")
        Image(
            painter = painterResource(id = R.drawable.menu_icon),
            contentDescription = "menu"
        )
    }
    LineColor(color = Color.LightGray)
}

@Composable
fun User(){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        CircleImage(image = R.drawable.test_avtuser, size = 100.dp, border = 2.dp)
        TextBold(
            text = "@ductihong82",
            Modifier.padding(10.dp)
        )
        Row {
            Infor(number = "17", name = "Following")
            Infor(number = "17", name = "Followers")
            Infor(number = "17", name = "Likes")
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row {
            TextBold(
                text = "Edit profile",
                Modifier
                    .border(1.dp, Color.LightGray)
                    .padding(60.dp, 10.dp)
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = "Tap to add bio", color = Color.Gray)
    }
}

@Composable
fun Infor(number: String, name:String){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ){
        TextBold(text = number)
        Text(text = name, color = Color.Gray)
    }
}

@Composable
fun UserVideo(){
    Column {
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray)
        ){
            TabItem(image = R.drawable.tabs_icon)
            TabItem(image = R.drawable.hearthide_icon)
        }
    }
}

@Composable
fun TabItem(image:Int){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding()
    ){
        Image(
            painter = painterResource(id = image),
            contentDescription = "tab",
            Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier
            .width(40.dp)
            .height(2.dp)
            .background(Color.Black))
    }
}