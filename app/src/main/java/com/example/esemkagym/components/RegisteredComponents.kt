package com.example.esemkagym.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esemkagym.R

@Composable
fun RegisterText(){
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 80.dp),
        contentAlignment = Alignment.Center,
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "You are registered!",
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp,
                fontStyle = FontStyle.Normal,
                color = Color.Black
            )
            
            Text(
                modifier = Modifier
                    .padding(top = 30.dp),
                text = stringResource(id = R.string.register),
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 20.sp,
                color = Color.Black,
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(id = R.string.member),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                color = Color.LightGray
            )
        }
    }
}