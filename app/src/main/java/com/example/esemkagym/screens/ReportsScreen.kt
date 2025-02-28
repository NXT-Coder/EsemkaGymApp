package com.example.esemkagym.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.esemkagym.components.HeaderReport
import com.example.esemkagym.components.TodayAttendance
import com.example.esemkagym.viewmodel.AttendanceViewModel
import com.example.esemkagym.viewmodel.ReportViewModel

@Composable
fun ReportsScreen(reportViewModel: ReportViewModel, attendanceViewModel: AttendanceViewModel){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            HeaderReport(reportViewModel, attendanceViewModel)
        }
    }
}
