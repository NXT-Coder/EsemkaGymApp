package com.example.esemkagym.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Header(){
    Text(
        text = "Daily CheckIn Code",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        textAlign = TextAlign.Center,
        fontStyle = FontStyle.Normal
    )
}

@Composable
fun Body(code: String){
    val date = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatted = date.format(formatter)

    Box(modifier = Modifier.fillMaxWidth()){
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = formatted,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
            )
            Text(
                text = code,
                fontWeight = FontWeight.Bold,
                fontSize = 55.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}