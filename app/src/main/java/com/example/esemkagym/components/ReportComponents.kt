package com.example.esemkagym.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esemkagym.R
import com.example.esemkagym.dataclass.Attendance
import com.example.esemkagym.viewmodel.AttendanceViewModel
import com.example.esemkagym.viewmodel.ReportViewModel

@Composable
fun HeaderReport(reportViewModel: ReportViewModel, attendanceViewModel: AttendanceViewModel){
    Column {
        Text(
            text = "Reports",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 35.dp, bottom = 15.dp),
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Medium,
            fontSize = 28.sp
        )
        TodayAttendance(viewModel = reportViewModel)
        TodayGenderChart(reportViewModel = reportViewModel)
        AttendanceLog(attendanceViewModel)
    }
}

@Composable
fun TodayAttendance(viewModel: ReportViewModel){
    val totalCheckIns = viewModel.getTotalCheckIns()

    Log.d("CheckTotal", "$totalCheckIns")
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp)
    ){
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Today's attendance",
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp)) // Memberikan sedikit ruang antara teks dan divider
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                val textLengthPercentage = 0.6f // Menentukan persentase panjang divider hitam sesuai dengan panjang teks

                // Divider abu-abu
                Box(
                    modifier = Modifier
                        .fillMaxWidth(textLengthPercentage)
                        .height(5.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.LightGray)
                )

                // Divider hitam
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.Black)
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 30.dp, start = 20.dp, end = 20.dp),
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = totalCheckIns.toString(),
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        fontSize = 60.sp
                    )
                    Text(
                        text = "People",
                        fontWeight = FontWeight.Medium,
                        fontSize = 35.sp,
                        fontStyle = FontStyle.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun TodayGenderChart(reportViewModel: ReportViewModel){
    val (maleCount, femaleCount) = reportViewModel.getGenderCounts()
    val totalCount = reportViewModel.getTotalCheckIns()

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp, top = 60.dp)
    ){
        Column {
            Text(
                text = "Today's Gender Chart",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.heightIn(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val textWidth = 0.55f;
                Box(modifier = Modifier
                    .fillMaxWidth(textWidth)
                    .clip(RoundedCornerShape(5.dp))
                    .height(5.dp)
                    .background(Color.LightGray)
                )

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .height(5.dp)
                    .background(Color.Black)
                )
            }

            GenderBarChart(
                maleCount = maleCount,
                femaleCount = femaleCount,
                totalCount = totalCount
            )
        }
    }
}

@Composable
fun GenderBarChart(
    maleCount: Int,
    femaleCount: Int,
    totalCount: Int
) {
    val totalWidth = 180.dp // Width of the chart container
    val maleWidth = if (totalCount > 0) (maleCount / totalCount.toFloat()) * totalWidth.value else 0f
    val femaleWidth = if (totalCount > 0) (femaleCount / totalCount.toFloat()) * totalWidth.value else 0f

    Log.d("maleWidth", "$maleWidth")
    Log.d("femaleWidth", "$femaleWidth")
    Log.d("maleCount", "$maleCount")
    Log.d("femaleCount", "$femaleCount")

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(colorResource(id = R.color.orange)),
                )
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = "Male",
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal
                )

                Spacer(modifier = Modifier.width(20.dp))

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(colorResource(id = R.color.cream)),
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Female",
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal
                )
            }

            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(maleWidth.dp)
                            .height(25.dp)
                            .background(colorResource(id = R.color.orange)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = maleCount.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 5.dp),
                            textAlign = TextAlign.End,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(femaleWidth.dp)
                            .height(25.dp)
                            .background(colorResource(id = R.color.cream)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = femaleCount.toString(),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 5.dp),
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AttendanceLog(viewModel: AttendanceViewModel){
    val attendanceList by viewModel.attendanceList.observeAsState(emptyList())

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = 20.dp,
            end = 20.dp,
            top = 30.dp
        )){
        Column {
            Text(
                text = "Attendance Log",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontStyle = FontStyle.Normal
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val textWidth = 0.65f;

                Box(
                    modifier = Modifier
                        .fillMaxWidth(textWidth)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.LightGray)
                        .heightIn(5.dp),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .heightIn(5.dp)
                        .background(Color.Black),
                )
            }
            LazyColumn {
                items(attendanceList) { attendance ->
                    CustomAttendance(attendance)
                }
            }
        }
    }
}

@Composable
fun CustomAttendance(attendance: Attendance) {
    Log.d("Attendance", "Displaying Attendance: $attendance")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp
            ),
    ){
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = if(attendance.gender == "FEMALE") R.drawable.female else R.drawable.male),
                contentDescription = "Logo",
                modifier = Modifier.size(50.dp)
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp),
                text = attendance.userName,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp
            )
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f),
                text = attendance.checkIn,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp
            )
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f),
                text = attendance.checkOut,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                fontSize = 14.sp
            )
        }
    }
}