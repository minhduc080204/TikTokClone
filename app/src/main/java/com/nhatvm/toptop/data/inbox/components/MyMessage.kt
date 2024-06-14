package com.nhatvm.toptop.data.inbox.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.nhatvm.toptop.data.theme.lightblue

@Composable
fun MyMessage(content: String) {
    Box (
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = "${content}",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 19.sp,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp,
                        topEnd = 18.dp,
                    )
                )
                .background(lightblue)
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .align(Alignment.BottomEnd)
        )
    }
    Spacer(modifier = Modifier.size(5.dp))
}