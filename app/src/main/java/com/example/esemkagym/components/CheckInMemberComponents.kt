package com.example.esemkagym.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.esemkagym.R
import com.example.esemkagym.dataclass.Attendance
import com.example.esemkagym.viewmodel.CheckInViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun HeaderBar(onClick: () -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 15.dp, start = 20.dp, end = 20.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Daily CheckIn",
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                color = Color.Black,
                fontSize = 18.sp
            )
            Row(
                modifier = Modifier.padding(start = 35.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.round_logout_24),
                    contentDescription = "Logout",
                    modifier = Modifier.clickable {
                        onClick()
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Sign Out",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Normal,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun CheckIn(
    checkIn: String,
    onCheckChange: (String) -> Unit,
    onClick: () -> Unit
){
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val formattedDate = currentDate.format(formatter)

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 30.dp, start = 30.dp, end = 30.dp)){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = formattedDate,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 70.dp)
            )
            TextField(
                value = checkIn,
                onValueChange = onCheckChange,
                textStyle = TextStyle(textAlign = TextAlign.Center),
                placeholder = {
                    Text(
                        text = "CheckIn Code",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.heightIn(30.dp))
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .height(40.dp)
                    .width(130.dp),
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text(
                    text = "Check In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun Checkout(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 30.dp, end = 30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .height(40.dp)
                    .width(130.dp),
                colors = ButtonDefaults.buttonColors(Color.LightGray)
            ) {
                Text(
                    text = "Check Out",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun Attendances(checkInViewModel: CheckInViewModel){
    val attendances by checkInViewModel.checkIn.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        checkInViewModel.fetchAttendance()
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp, top = 50.dp)){
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Attendance Log",
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
            Divider(thickness = 3.dp, modifier = Modifier.width(120.dp), color = Color.Black)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Column(
                    modifier = Modifier.width(120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 15.dp),
                        text = "Check In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Normal
                    )
                    LazyColumn(modifier = Modifier.padding(bottom = 20.dp)) {
                        itemsIndexed(attendances) { index, item ->
                            val text = item.checkIn
                            val localDate = LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME)
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                            val formattedDate = localDate.format(formatter)
                            Text(
                                text = formattedDate,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 5.dp),
                                color = Color.LightGray
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.width(120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 15.dp),
                        text = "Check Out",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Normal,
                        color = Color.LightGray
                    )
                    LazyColumn(modifier = Modifier.padding(bottom = 20.dp)) {
                        itemsIndexed(attendances){ index, item ->
                            val text = item.checkOut
                            if (text == null || text == "null"){
                                Text(
                                    text = "",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Normal,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 5.dp),
                                    color = Color.LightGray
                                )
                            }else{
                                val localDate = LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME)
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                val formatted = localDate.format(formatter)
                                Text(
                                    text = formatted,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Normal,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 5.dp),
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}