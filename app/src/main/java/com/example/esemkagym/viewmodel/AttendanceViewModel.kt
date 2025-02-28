package com.example.esemkagym.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.esemkagym.dataclass.Attendance
import java.time.LocalDate

class AttendanceViewModel(private val context: Context) : ViewModel() {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _attendanceList = MutableLiveData<List<Attendance>>()
    val attendanceList: LiveData<List<Attendance>> = _attendanceList

    init {
        loadAttendance()
    }

    private fun loadAttendance() {
        val todayDate = LocalDate.now().toString()
        val attendanceList = mutableListOf<Attendance>()

        val allEntries = sharedPreferences.all
        val userIds = allEntries.keys.filter { it.startsWith("name_") && it.contains(todayDate) }
            .map { it.split("_")[1] }

        for (userId in userIds) {
            val id = sharedPreferences.getString("member_id_${userId}_$todayDate", "") ?: ""
            val userName = sharedPreferences.getString("name_${userId}_$todayDate", "") ?: ""
            val checkIn = sharedPreferences.getString("check_in_${userId}_$todayDate", "") ?: ""
            val checkOut = sharedPreferences.getString("check_out_${userId}_$todayDate", "") ?: ""
            val gender = sharedPreferences.getString("gender_${userId}_$todayDate", "") ?: ""

            if (userName.isNotEmpty()) {
                val attendance = Attendance(checkIn, checkOut, id, userId, userName, gender)
                attendanceList.add(attendance)
            }
        }

        Log.d("AttendanceSMK", "Loaded Attendance List: $attendanceList, $userIds, $allEntries")

        _attendanceList.value = attendanceList
    }
}