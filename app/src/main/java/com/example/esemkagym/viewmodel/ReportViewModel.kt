package com.example.esemkagym.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class ReportViewModel(private val sharedPreferences: SharedPreferences): ViewModel() {
    fun getTotalCheckIns(): Int {
        val todayDate = LocalDate.now().toString()
        val checkInMapString = sharedPreferences.getString("check_in_map_$todayDate", "") ?: ""
        val checkInMap = checkInMapString.split(",").filter { it.isNotEmpty() }
            .map {
                val parts = it.split(":")
                parts[0] to parts[1]
            }.toMap()

        return checkInMap.size
    }

    fun getGenderCounts(): Pair<Int, Int> {
        val todayDate = LocalDate.now().toString()
        val genderMapString = sharedPreferences.getString("gender_map_$todayDate", "") ?: ""
        val genderMap = genderMapString.split(",").filter { it.isNotEmpty() }
            .map {
                val parts = it.split(":")
                parts[0] to parts[1]
            }.toMap()

        val maleCount = genderMap.values.count { it == "MALE" }
        val femaleCount = genderMap.values.count { it == "FEMALE" }

        return Pair(maleCount, femaleCount)
    }
}