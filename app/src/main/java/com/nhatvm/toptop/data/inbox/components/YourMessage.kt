package com.nhatvm.toptop.data.inbox.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhatvm.toptop.data.components.CircleImage

@Composable
fun YourMessage(urlImage: String, content: String) {
    Box (
        modifier = Modifier.fillMaxWidth()
    ){
        Row (Modifier.align(Alignment.BottomStart)){
            CircleImage(imageUrl = urlImage, size = 35.dp)
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = "${content}",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 19.sp,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topEnd = 20.dp,
                            bottomEnd = 20.dp,
                            topStart = 18.dp,
                        )
                    )
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
    }
    Spacer(modifier = Modifier.size(5.dp))
}