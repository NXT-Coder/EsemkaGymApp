package com.example.esemkagym.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.esemkagym.dataclass.ActiveMember
import com.example.esemkagym.dataclass.InactiveMember
import com.example.esemkagym.dataclass.Member
import com.example.esemkagym.dataclass.PendingApproval
import com.example.esemkagym.sharedperferences.SharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ManageViewModel(private val context: Context) : ViewModel() {
    private val tokenManager = SharedPreference(context)

    private val _activeMember = MutableLiveData<List<ActiveMember>>()
    val activeMember: LiveData<List<ActiveMember>> get() = _activeMember

    private val _inactiveMember = MutableLiveData<List<InactiveMember>>()
    val inactiveMember: LiveData<List<InactiveMember>> get() = _inactiveMember

    private val _pendingMember = MutableLiveData<List<PendingApproval>>()
    val pendingMember: LiveData<List<PendingApproval>> get() = _pendingMember

    private val _filterMemberActive = MutableLiveData<List<ActiveMember>>()
    val filterActiveMember: LiveData<List<ActiveMember>> get() = _filterMemberActive

    private val _filterInactiveMember = MutableLiveData<List<InactiveMember>>()
    val filterInactiveMember: LiveData<List<InactiveMember>> get() = _filterInactiveMember

    private val _filterPendingMember = MutableLiveData<List<PendingApproval>>()
    val filterPendingMember: LiveData<List<PendingApproval>> get() = _filterPendingMember

    fun searchActiveMember(query: String){
        val allActiveMember = _activeMember.value ?: emptyList()
        val filteredList = if (query.isEmpty()){
            allActiveMember
        }else{
            allActiveMember.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        _filterMemberActive.value = filteredList
    }

    fun searchInactiveMember(query: String){
        val allInactiveMember = _inactiveMember.value ?: emptyList()
        val filteredList = if (query.isEmpty()){
            allInactiveMember
        }else{
            allInactiveMember.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        _filterInactiveMember.value = filteredList
    }

    fun searchPendingMember(query: String){
        val allPendingMember = _pendingMember.value ?: emptyList()
        val filteredList = if (query.isEmpty()){
            allPendingMember
        }else{
            allPendingMember.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        _filterPendingMember.value = filteredList
    }

    fun fetchActiveMember(){
        viewModelScope.launch {
            val activeMembers = getActiveMember()
            _activeMember.postValue(activeMembers)
        }
    }

    fun fetchInactiveMember(){
        viewModelScope.launch {
            val inactiveMembers = getInactiveMember()
            _inactiveMember.postValue(inactiveMembers)
        }
    }

    fun fetchPendingMember(){
        viewModelScope.launch {
            val pendingMembers = getPendingMemberFromApi()
            _pendingMember.postValue(pendingMembers)
        }
    }

    private suspend fun getActiveMember() : List<ActiveMember> {
        return withContext(Dispatchers.IO){
            val token = tokenManager.getToken().toString().replace(Regex("[\"']"), "")
            val url = URL("http://192.168.1.6:8081/api/member?status=ACTIVE")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.setRequestProperty("Content-Type", "application/json")

            return@withContext try{
                val response = connection.responseCode
                if(response == HttpURLConnection.HTTP_OK){
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(responseStream)
                    val activeMembers = mutableListOf<ActiveMember>()

                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val active = ActiveMember(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            registerAt = jsonObject.getString("registerAt"),
                            membershipEnd = jsonObject.getString("membershipEnd"),
                            joinedMemberAt = jsonObject.getString("joinedMemberAt")
                        )
                        activeMembers.add(active)
                    }
                    activeMembers
                }else{
                    Log.e("API ERROR", "Response Code: $response")
                    emptyList()
                }
            }finally {
                connection.disconnect()
            }
        }
    }

    private suspend fun getInactiveMember(): List<InactiveMember> {
        return withContext(Dispatchers.IO){
            val token = tokenManager.getToken().toString().replace(Regex("[\"']"), "")
            val url = URL("http://192.168.1.6:8081/api/member?status=INACTIVE")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.setRequestProperty("Content-Type", "application/json")

            return@withContext try{
                val response = connection.responseCode
                if(response == HttpURLConnection.HTTP_OK){
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(responseStream)
                    val inactiveMembers = mutableListOf<InactiveMember>()

                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val inactiveMember = InactiveMember(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            registerAt = jsonObject.getString("registerAt"),
                            membershipEnd = jsonObject.getString("membershipEnd"),
                            joinedMemberAt = jsonObject.getString("joinedMemberAt")
                        )
                        inactiveMembers.add(inactiveMember)
                    }
                    inactiveMembers
                }else{
                    Log.e("API Error", "Response Code: $response")
                    emptyList()
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    private suspend fun getPendingMemberFromApi(): List<PendingApproval>{
        return withContext(Dispatchers.IO){
            val token = tokenManager.getToken().toString().replace(Regex("[\"']"), "")
            val url = URL("http://192.168.1.6:8081/api/member?status=PENDING_APPROVAL")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.setRequestProperty("Content-Type", "application/json")

            return@withContext try{
                val response = connection.responseCode
                if(response == HttpURLConnection.HTTP_OK){
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(responseStream)
                    val pendingMembers = mutableListOf<PendingApproval>()

                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val pendingMember = PendingApproval(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            registerAt = jsonObject.getString("registerAt"),
                            membershipEnd = jsonObject.getString("membershipEnd"),
                            joinedMemberAt = jsonObject.getString("joinedMemberAt")
                        )
                        pendingMembers.add(pendingMember)
                    }
                    pendingMembers
                }else{
                    Log.e("API Error", "Response : $response")
                    emptyList()
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    suspend fun resumeMember(memberId: String): String {
        return withContext(Dispatchers.IO){
            val token = tokenManager.getToken().toString().replace(Regex("[\"']"), "")
            val url = URL("http://192.168.1.6:8081/api/member/$memberId/resume")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "PUT"
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val jsonString = JSONObject().apply {
                put("registerAt", memberId)
            }.toString()

            try{
                OutputStreamWriter(connection.outputStream).use {os ->
                    os.write(jsonString)
                    os.flush()
                }

                val response = connection.responseCode
                if(response == HttpURLConnection.HTTP_OK){
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
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

    suspend fun approveMember(memberId: String){
        return withContext(Dispatchers.IO){
            val token = tokenManager.getToken().toString().replace(Regex("[\"']"), "")
            val url = URL("http://192.168.1.6:8081/api/member/$memberId/approve")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "PUT"
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val jsonString = JSONObject().apply {
                put("registerAt", memberId)
            }.toString()

            try {
                OutputStreamWriter(connection.outputStream).use {os ->
                    os.write(jsonString)
                    os.flush()
                }

                val response = connection.responseCode
                if(response == HttpURLConnection.HTTP_OK){
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    responseStream
                }else{
                    val errorStream = connection.errorStream.bufferedReader().use { it.readText() }
                    errorStream
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    private suspend fun getMembersFromApi(name: String, status: String): List<Member> {
        return withContext(Dispatchers.IO) {
            val token = tokenManager.getToken().toString().replace(Regex("[\"']"), "")
            val url = URL("http://192.168.1.6:8081/api/member?name=$name&status=$status")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer $token")
            connection.setRequestProperty("Content-Type", "application/json")

            return@withContext try {
                val response = connection.responseCode
                if (response == HttpURLConnection.HTTP_OK) {
                    val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(responseStream)
                    val members = mutableListOf<Member>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val member = Member(
                            id = jsonObject.getInt("id"),
                            name = jsonObject.getString("name"),
                            registerAt = jsonObject.getString("registerAt"),
                            membershipEnd = jsonObject.getString("membershipEnd"),
                            joinedMemberAt = jsonObject.getString("joinedMemberAt"),
                            status = status
                        )
                        members.add(member)
                    }
                    members
                } else {
                    Log.e("API Error", "ResponseCode: $response")
                    emptyList()
                }
            } finally {
                connection.disconnect()
            }
        }
    }
}