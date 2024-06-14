package com.nhatvm.toptop.data.inbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.nhatvm.toptop.data.R
import com.nhatvm.toptop.data.Routes
import com.nhatvm.toptop.data.components.CircleImage
import com.nhatvm.toptop.data.components.TextBold
import com.nhatvm.toptop.data.inbox.components.MyMessage
import com.nhatvm.toptop.data.inbox.components.YourMessage
import com.nhatvm.toptop.data.theme.lightgray
import kotlinx.coroutines.launch

@Composable
fun AIChatScreen(onBack:() -> Unit) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var prompt by remember { mutableStateOf("") }
    var response by remember {
        mutableStateOf(listOf("Xin chào tôi là TopTOP AI !"))
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(lightgray)
            .padding(10.dp)
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 5.dp)
        ){
            Image(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onBack()
                    }
            )
            Spacer(modifier = Modifier.size(20.dp))
            CircleImage(imageUrl = Routes.AVT_AI, size = 45.dp)
            Spacer(modifier = Modifier.size(10.dp))
            TextBold(username = "TopTop AI", color = Color.Black, fontsize = 20.sp)
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
        LazyColumn (
            state = listState,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.weight(1f)
        ){
            items(1){
                for ( i in 0..response.size-1){
                    if (i%2==0){
                        YourMessage(
                            urlImage = Routes.AVT_AI,
                            content = response[i]
                        )
                    }else{
                        MyMessage(response[i])
                    }
                }
            }
        }
        Spacer(modifier = Modifier.size(15.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .fillMaxWidth()
                .background(Color.White)
        ){
            TextField(
                value = prompt,
                onValueChange = { prompt = it },
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                placeholder = { Text(text = "Say something...", color = Color.LightGray, fontSize = 18.sp) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.sendcomment_icon),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        val _promt = prompt
                        response += prompt
                        prompt = ""
                        coroutineScope.launch {
                            getResponse(_promt){
                                it?.let {
                                    response += it
                                }
                            }
                            listState.scrollToItem(response.lastIndex)
                        }
                    }
            )
        }

    }

}

suspend fun getResponse(question: String, callback: (String?) -> Unit) {
    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyC-cBuzDEDfWw5e7hL0jdT4rI580msJuQM"
    )
    var fullResponse = ""
    val inputContent = content {
        text("$question")
    }
    generativeModel.generateContentStream(inputContent).collect { chunk ->
        fullResponse += chunk.text
    }
    callback(fullResponse)
}