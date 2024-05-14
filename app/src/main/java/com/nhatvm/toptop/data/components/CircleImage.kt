package com.nhatvm.toptop.data.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CircleImage(image: Int, size:Dp, border:Dp = 0.dp, color: Color = Color.White, modifier:Modifier = Modifier){
    Image(
        painter = painterResource(id = image),
        contentDescription = "image",
        modifier = modifier
            .size(size)
            .clip(shape = CircleShape)
            .border(
                border,
                color,
                shape = CircleShape
            )
    )
}