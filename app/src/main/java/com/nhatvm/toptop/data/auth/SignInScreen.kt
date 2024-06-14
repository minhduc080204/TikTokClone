package com.nhatvm.toptop.data.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.auth.components.InputType
import com.nhatvm.toptop.data.auth.components.TextInput
import com.nhatvm.toptop.data.video.LOADING

@Composable
fun SignInScreen(
    onSignIn: (String, String) -> Unit,
    onSignUp: () -> Unit,
    isLoading: Boolean,
){
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    if (isLoading){
        LOADING()
    }else{
        Column(
            Modifier
                .background(Color.Black)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            Image(
                painter = painterResource(id = R.drawable.toptop_logo),
                contentDescription = "Logo"
            )

            Text(
                text = "Đăng nhập TopTop",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            TextInput(
                inputType = InputType.Email,
                onValueChanged = { email = it },
                keyboardActions = KeyboardActions(onNext = {
                    passwordFocusRequester.requestFocus()
                })
            )
            TextInput(
                inputType = InputType.Password,
                onValueChanged = { password = it },
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    onSignIn(email, password)
                }),
                focusRequester = passwordFocusRequester
            )
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onSignIn(email, password)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SIGN IN", Modifier.padding(vertical = 8.dp))
            }
            Divider(
                color = Color.White.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(top = 48.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account?", color = Color.White)
                TextButton(onClick = { onSignUp() }) {
                    Text("SIGN UP")
                }
            }
        }
    }

}