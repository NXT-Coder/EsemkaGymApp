package com.example.esemkagym.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.esemkagym.components.RegisterText

@Composable
fun RegisteredScreen(){
    Surface(modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            RegisterText()
        }
    }
}

@Preview
@Composable
fun RegisteredPreview(){
    RegisteredScreen()
}