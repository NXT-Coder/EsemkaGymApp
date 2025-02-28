package com.example.esemkagym.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esemkagym.dataclass.User
import com.example.esemkagym.sharedperferences.SharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignUpViewModel(private val context: Context) : ViewModel() {
    fun signUp(email: String, password: String, name: String, gender: String) {
        viewModelScope.launch {
            try {
                signUpRequest(email, password, name, gender)
            } catch (e: Exception) {
                Toast.makeText(context, "Username is already exist, please choose another one", Toast.LENGTH_LONG).show()
                // Handle any exceptions here (e.g., show a toast or log the error)
                Log.e("SignUpError", "Error during sign-up: ${e.message}")
            }
        }
    }

    private suspend fun signUpRequest(email: String, password: String, name: String, gender: String) {
        withContext(Dispatchers.IO) {
            val url = URL("http://192.168.1.6:8081/api/signup")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val jsonString = JSONObject().apply {
                put("email", email)
                put("gender", gender)
                put("name", name)
                put("password", password)
            }.toString()

            Log.d("InputString", jsonString)

            try {
                OutputStreamWriter(connection.outputStream).use { os ->
                    os.write(jsonString)
                    os.flush()
                }

                val response = connection.responseCode
                if (response == HttpURLConnection.HTTP_OK) {
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d("NetworkCheck", responseStream)
                    // Handle success (e.g., navigate to another screen or show a success message)
                } else {
                    val errorStream = connection.errorStream.bufferedReader().use { it.readText() }
                    Log.d("NetworkError", errorStream)
                    // Handle error (e.g., show an error message)
                }
            } finally {
                connection.disconnect()
            }
        }
    }
}