package com.nhatvm.toptop.data.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Header(
    modifier: Modifier = Modifier,
    isTabSelectedIndex: Int,
    onFollowingTab: () -> Unit,
    onForyouTab: () -> Unit,
){
    Row (
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        HeaderItem(
            title = "Following",
            isSelected = isTabSelectedIndex == 0,
            isForU = false,
            onSelectedTab = onFollowingTab
        )
        Spacer(modifier = Modifier.width(12.dp))
        HeaderItem(
            title = "For You",
            isSelected = isTabSelectedIndex == 1,
            isForU = true,
            onSelectedTab = onForyouTab
        )
    }
}

@Composable
fun HeaderItem(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    isForU: Boolean,
    onSelectedTab: () -> Unit
) {
    val alpha = if (isSelected) 1f else 0.6f

    Column(
        modifier = modifier
            .wrapContentSize()
            .clickable { onSelectedTab() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6.copy(color = Color.White.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (isSelected) {
            Divider(color = Color.White, thickness = 2.dp, modifier = Modifier.width(24.dp))
        }
    }
}
