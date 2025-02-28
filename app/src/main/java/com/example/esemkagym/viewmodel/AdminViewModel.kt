package com.example.esemkagym.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esemkagym.sharedperferences.SharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AdminViewModel(private val context: Context) : ViewModel() {
    private val tokenManager = SharedPreference(context)
    private val _codeResult = MutableLiveData<String>()
    val codeResult: LiveData<String> get() = _codeResult

    fun fetchCodeFromApi() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val token = tokenManager.getToken().toString().replace(Regex("[\"']"), "")
                    val url = URL("http://192.168.1.6:8081/api/attendance/checkin/code")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", "Bearer $token")

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                        val jsonObject = JSONObject(responseStream)
                        jsonObject.getString("code")
                    } else {
                        Log.e("API Error", "ResponseCode: $responseCode")
                        ""
                    }
                }
                _codeResult.postValue(result)
            } catch (e: Exception) {
                Log.e("API Error", "Exception: ${e.message}")
            }
        }
    }
}