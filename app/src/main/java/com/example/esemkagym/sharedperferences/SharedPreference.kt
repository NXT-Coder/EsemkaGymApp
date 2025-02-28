package com.example.esemkagym.sharedperferences

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate

class SharedPreference(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String, isLogin: Boolean){
        sharedPreferences.edit().putString("auth_token", token).apply()
        sharedPreferences.edit().putBoolean("is_login", isLogin).apply()
    }

    fun saveLogAdmin(isAdmin: Boolean){
        sharedPreferences.edit().putBoolean("is_admin", isAdmin).apply()
    }

    fun saveLogMember(joinedMemberAt: String){
        sharedPreferences.edit().putString("joinedMemberAt", joinedMemberAt).apply()
    }

    fun saveAttendanceLog(
        memberId: String,
        checkIn: String,
        checkOut: String,
        userId: String,
        name: String,
        gender: String
    ) {
        val editor = sharedPreferences.edit()

        // Dapatkan tanggal hari ini
        val todayDate = LocalDate.now().toString()

        // Simpan data attendance dengan kunci yang mengandung tanggal
        editor.putString("member_id_${memberId}_$todayDate", memberId)
        editor.putString("check_in_${memberId}_$todayDate", checkIn)
        editor.putString("check_out_${memberId}_$todayDate", checkOut)
        editor.putString("name_${userId}_$todayDate", name)
        editor.putString("gender_${userId}_$todayDate", gender)

        // Simpan data check-in
        val checkInMapString = sharedPreferences.getString("check_in_map_$todayDate", "") ?: ""
        val checkInMap = checkInMapString.split(",").filter { it.isNotEmpty() }
            .map {
                val parts = it.split(":")
                parts[0] to parts[1]
            }.toMap().toMutableMap()

        // Simpan check-in hanya jika belum ada check-in untuk userId tersebut
        if (!checkInMap.containsKey(userId)) {
            checkInMap[userId] = checkIn
        }

        // Simpan kembali ke SharedPreferences
        val checkInMapStringNew = checkInMap.entries.joinToString(",") { "${it.key}:${it.value}" }
        editor.putString("check_in_map_$todayDate", checkInMapStringNew)
        editor.apply()

        // Save gender data
        val genderMapString = sharedPreferences.getString("gender_map_$todayDate", "") ?: ""
        val genderMap = genderMapString.split(",").filter { it.isNotEmpty() }
            .map {
                val parts = it.split(":")
                parts[0] to parts[1]
            }.toMap().toMutableMap()

        if (!genderMap.containsKey(userId)) {
            genderMap[userId] = gender
        }

        val genderMapStringNew = genderMap.entries.joinToString(",") { "${it.key}:${it.value}" }
        editor.putString("gender_map_$todayDate", genderMapStringNew)
        editor.apply()
    }

    fun getToken(): String?{
        return sharedPreferences.getString("auth_token", null)
    }

    fun getIsLogged() : Boolean{
        return sharedPreferences.getBoolean("is_login", false)
    }

    fun getIsAdmin(): Boolean{
        return sharedPreferences.getBoolean("is_admin", false)
    }

    fun getIsJoined(): String?{
        return sharedPreferences.getString("joinedMemberAt", null)
    }

    fun clearToken(){
        sharedPreferences.edit().remove("auth_token").apply()
        sharedPreferences.edit().remove("is_login").apply()
        sharedPreferences.edit().remove("is_admin").apply()
    }
}