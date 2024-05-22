package com.nhatvm.toptop.data.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

sealed class InputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation,
    val keyboardType: KeyboardType? = null
) {
    object UserName : InputType(
        label = "Username",
        icon = Icons.Default.AccountCircle,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None,
        keyboardType = KeyboardType.Text
    )

    object Email : InputType(
        label = "Email",
        icon = Icons.Default.MailOutline,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None,
        keyboardType = KeyboardType.Text
    )

    object Name : InputType(
        label = "Name",
        icon = Icons.Default.Face,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None,
        keyboardType = KeyboardType.Text
    )


    object Phone : InputType(
        label = "Phone",
        icon = Icons.Default.Phone,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None,
        keyboardType = KeyboardType.Phone
    )

    object Password : InputType(
        label = "Password",
        icon = Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = PasswordVisualTransformation(),
        keyboardType = KeyboardType.Password
    )
}

@Composable
fun TextInput(
    inputType: InputType,
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions,
    onValueChanged: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }

    TextField(
        value = value,
        onValueChange = {
            value = it
            onValueChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusOrder(focusRequester ?: FocusRequester()),
        leadingIcon = { Icon(imageVector = inputType.icon, null, tint = Color.Black) },
        label = { Text(text = inputType.label, style = TextStyle(Color.Red)) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            textColor = Color.Black
        ),
        singleLine = true,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        keyboardActions = keyboardActions
    )
}