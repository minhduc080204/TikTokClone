package com.nhatvm.toptop.data.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.Screens.Routes

@Composable
fun TabBar(navController: NavController) {
    Row (
        horizontalArrangement =  Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(10.dp)
    ){
        TopBarItem(text = "Home", image = R.drawable.home_icon, {
            navController.navigate(Routes.HOME_SCREEN)
        })
        TopBarItem(text = "Discover", image = R.drawable.search_icon, {})
        TopBarItem(text = "", image = R.drawable.upload_icon, {})
        TopBarItem(text = "Input", image = R.drawable.message_icon, {})
        TopBarItem(text = "Me", image = R.drawable.account_icon, {
            navController.navigate(Routes.USER_SCREEN)
        })
    }
}

@Composable
fun TopBarItem(text:String, image:Int, onClick:() ->Unit){
    Column (
        horizontalAlignment =  Alignment.CenterHorizontally,
        modifier = Modifier
            .width(75.dp)
            .clickable {
                onClick()
            }
    ){
        Image(
            painter = painterResource(id = image),
            contentDescription = text,
//            modifier = Modifier.background(Color.White)
        )
        Text(
            text = text,
            color = Color.White
        )
    }
}