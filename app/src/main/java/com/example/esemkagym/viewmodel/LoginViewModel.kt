package com.example.esemkagym.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esemkagym.dataclass.User
import com.example.esemkagym.sharedperferences.SharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginViewModel(private val context: Context) : ViewModel() {
    private val tokenManager = SharedPreference(context)
    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> get() = _loginResult

    fun login(email: String, password: String){
        viewModelScope.launch {
            try {
                val user: User? = performLogin(email, password)
                if(user != null && user.token != null){
                    tokenManager.saveToken(user.token, true)
                    tokenManager.saveLogAdmin(user.isAdmin)
                    _loginResult.postValue(Result.success(user))
                }else{
                    _loginResult.postValue(Result.failure(Exception("Username or password wrong")))
                }
            }catch (e: Exception){
                _loginResult.postValue(Result.failure(e))
            }
        }
    }

    fun logout(){
        tokenManager.clearToken()
    }

    private suspend fun performLogin(email: String, password: String): User? {
        return withContext(Dispatchers.IO){
            val url = URL("http://192.168.1.6:8081/api/login")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val jsonString = JSONObject().apply {
                put("email", email)
                put("password", password)
            }.toString()

            try {
                OutputStreamWriter(connection.outputStream).use { os ->
                    os.write(jsonString)
                    os.flush()
                }

                val response = connection.responseCode
                if(response == HttpURLConnection.HTTP_OK){
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    parseUser(responseStream)
                }else{
                    val errorStream = connection.errorStream.bufferedReader().use { it.readText() }
                    Log.e("LoginError", errorStream)
                    null
                }
            }finally {
                connection.disconnect()
            }
        }
    }

    private fun parseUser(response: String): User {
        val jsonObject = JSONObject(response)
        val userObject = jsonObject.getJSONObject("user")
        val token = jsonObject.getString("token")

        return User(
            id = userObject.getInt("id"),
            name = userObject.getString("name"),
            email = userObject.getString("email"),
            gender = userObject.getString("gender"),
            isAdmin = userObject.getBoolean("admin"),
            joinedMemberAt = userObject.getString("joinedMemberAt"),
            token = token
        )
    }
}