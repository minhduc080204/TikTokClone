package com.nhatvm.toptop.data.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.auth.repositories.User
import com.nhatvm.toptop.data.components.CircleImage

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpdateProfileScreen(user: User, onBack:() -> Unit, onUpdate:(String, String, String) -> Unit) {
    var name by remember {
        mutableStateOf(user.Name)
    }
    var phone by remember {
        mutableStateOf(user.Phone)
    }
    var username by remember {
        mutableStateOf(user.Username)
    }
    val userrnameFocusRequester = remember { FocusRequester() }
    val phoneFocusRequester = remember { FocusRequester() }
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp)
    ){
        Column (
            modifier = Modifier
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Image(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "Back",
                    modifier = Modifier.clickable {
                        onBack()
                    }
                )
                TextBold(text = "Sửa hồ sơ")
                Spacer(modifier = Modifier.size(30.dp))
            }
            Spacer(modifier = Modifier.size(30.dp))
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ){
                CircleImage(image = R.drawable.test_avtuser, size = 70.dp)
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = "Thay đổi ảnh")
            }
            Text(text = "Giới thiệu về bạn")
            InputItem("Tên", name, onValueChange = { name = it },
                keyboardActions = KeyboardActions(onNext = {
                    userrnameFocusRequester.requestFocus()
                })
            )
            InputItem("Username", username, onValueChange = { username = it },
                keyboardActions = KeyboardActions(onNext = {
                    phoneFocusRequester.requestFocus()
                }),
                focusRequester = userrnameFocusRequester
            )
            Spacer(modifier = Modifier.size(10.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                TextBold(text = "Số điện thoại")
                OutlinedTextField(
                    value = phone, onValueChange = {it -> phone=it },
                    label = { Text(text = "Số điện thoại") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    keyboardActions = KeyboardActions(onDone = {
                        onUpdate(name, phone, username)
                    }),
                    modifier = Modifier.width(300.dp).focusOrder(phoneFocusRequester)
                )
            }
        }
        Button(
            onClick = { onUpdate(name, phone, username) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cập nhật thông tin")
        }
    }
}

@Composable
fun InputItem(
    head:String,
    value:String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    focusRequester: FocusRequester? = null,
) {
    Spacer(modifier = Modifier.size(10.dp))
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        TextBold(text = head)
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            label = { Text(text = head) },
            modifier = Modifier.width(300.dp)
                .focusOrder(focusRequester ?: FocusRequester()),
            keyboardActions = keyboardActions,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
    }
}