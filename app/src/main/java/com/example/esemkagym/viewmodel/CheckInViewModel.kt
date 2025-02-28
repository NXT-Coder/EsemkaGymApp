package com.example.esemkagym.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esemkagym.dataclass.Attendance
import com.example.esemkagym.sharedperferences.SharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class CheckInViewModel(private val context: Context) : ViewModel() {
    private val tokenManager = SharedPreference(context)
    private val _checkIn = MutableLiveData<List<Attendance>>()
    val checkIn: LiveData<List<Attendance>> get() = _checkIn

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun isCheckedIn(): Boolean {
        return sharedPreferences.getBoolean("isCheckedIn", false)
    }

    fun isCheckingOut(): Boolean {
        return sharedPreferences.getBoolean("isCheckingOut", true)
    }

    fun saveCheckInStatus(isCheckedIn: Boolean) {
        sharedPreferences.edit().putBoolean("isCheckedIn", isCheckedIn).apply()
    }

    fun fetchAttendance(){
        viewModelScope.launch {
            val attendances = getAttendanceFromApi("2023-04-05 13:00:00", "2027-04-05 13:00:00")
            _checkIn.postValue(attendances)
            Log.d("CheckAttendance", "$attendances")
        }
    }

    private suspend fun getAttendanceFromApi(fromDate: String, toDate: String) : List<Attendance>{
        return withContext(Dispatchers.IO){
            val token = tokenManager.getToken()
            val tokenString = token.toString()
            val cleanToken = tokenString.replace(Regex("[\"']"), "")
            val url = URL("http://192.168.1.6:8081/api/attendance?from=$fromDate&to=$toDate")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $cleanToken")
            connection.setRequestProperty("Accept", "application/json")

            return@withContext try{
                val responseCode = connection.responseCode
                Log.d("Response", "$responseCode")
                if(responseCode == HttpURLConnection.HTTP_OK){
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(responseStream)
                    val attendances = mutableListOf<Attendance>()

                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val userObject = jsonObject.getJSONObject("user")

                        val attendance = Attendance(
                            checkIn = jsonObject.getString("checkIn"),
                            checkOut = jsonObject.getString("checkOut"),
                            id = jsonObject.getString("id"),
                            userId = userObject.getString("id"),
                            userName = userObject.getString("name"),
                            gender = userObject.getString("gender")
                        )
                        attendances.add(attendance)
                    }
                    Log.d("Atten", "$attendances")
                    attendances
                }else{
                    Log.e("API Error", "ResponseCode: $responseCode")
                    emptyList()
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    suspend fun performCheckout(): Result<String> {
        return withContext(Dispatchers.IO) {
            val token = tokenManager.getToken().toString().replace(Regex("[\"']"), "")
            val url = URL("http://192.168.1.6:8081/api/attendance/checkout")
            val connection = url.openConnection() as HttpURLConnection
            return@withContext try {
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Authorization", "Bearer $token") // Adjust this if you use token or other authentication

                // Send request
                connection.outputStream.use { outputStream ->
                    val requestBody = "{}" // If you need to send data, add it here
                    outputStream.write(requestBody.toByteArray())
                }

                // Read response
                val responseCode = connection.responseCode
                val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

                // Handle response
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("CheckoutSuccess", responseMessage)
                    Result.success(responseMessage)
                } else {
                    Log.e("CheckoutError", "Error $responseCode: $responseMessage")
                    Result.failure(Exception("HTTP Error $responseCode: $responseMessage"))
                }
            } catch (e: Exception) {
                Log.e("CheckoutException", "Exception during checkout", e)
                Result.failure(e)
            } finally {
                connection.disconnect()
            }
        }
    }

    suspend fun performCheckIn(checkIn: String) : String {
        return withContext(Dispatchers.IO){
            val token = tokenManager.getToken()
            val tokenClean = token.toString()
            val cleanToken = tokenClean.replace(Regex("[\"']"), "")
            val url = URL("http://192.168.1.6:8081/api/attendance/checkin/$checkIn")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $cleanToken")
            connection.doOutput = true

            val jsonString = JSONObject().apply {
                put("checkIn", checkIn)
            }.toString()

            try{
                OutputStreamWriter(connection.outputStream).use { os ->
                    os.write(jsonString)
                    os.flush()
                }

                val response = connection.responseCode
                if(response == HttpURLConnection.HTTP_OK){
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(responseStream)
                    val userObject = jsonObject.getJSONObject("user")
                    val memberId = jsonObject.getString("id").toString()
                    val CheckIn = jsonObject.getString("checkIn").toString()
                    val checkOut = jsonObject.getString("checkOut").toString()
                    val userId = userObject.getString("id").toString()
                    val userName = userObject.getString("name").toString()
                    val gender = userObject.getString("gender").toString()

                    tokenManager.saveAttendanceLog(memberId, CheckIn, checkOut, userId, userName, gender)
                    Log.d("CheckInput", "$memberId, $CheckIn, $checkOut, $userId, $userName, $gender")
                    responseStream
                }else{
                    val errorStream = connection.errorStream.bufferedReader().use { it.readText() }
                    errorStream
                }
            }finally {
                connection.disconnect()
            }
        }
    }
}