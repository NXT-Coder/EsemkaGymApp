package com.example.esemkagym.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.esemkagym.components.ClickAbleText
import com.example.esemkagym.components.EmailTextField
import com.example.esemkagym.components.HeaderLogin
import com.example.esemkagym.components.ImageLogo
import com.example.esemkagym.components.LogButton
import com.example.esemkagym.components.PasswordTextField
import com.example.esemkagym.sharedperferences.SharedPreference
import com.example.esemkagym.viewmodel.LoginViewModel
import com.example.esemkagym.viewmodelfactory.LoginViewModelFactory

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context))
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    val loginResult by loginViewModel.loginResult.observeAsState()

    // Check if user is already logged in
    val tokenManager = remember { SharedPreference(context) }
    if (tokenManager.getIsLogged()) {
        // Navigate to the appropriate screen based on admin status
        val isAdmin = tokenManager.getIsAdmin() // Assuming you store this in shared preferences
        if (isAdmin) {
            navController.navigate("dailyAdmin")
        }else{
            navController.navigate("checkIn")
        }
    }

    Surface(modifier = Modifier
        .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()){
          Column {
              ImageLogo()
              HeaderLogin()
              EmailTextField(
                  email = email,
                  onEmailChange = {
                      email = it
                  },
              )
              Spacer(modifier = Modifier.height(15.dp))
              PasswordTextField(
                  password = password,
                  onPasswordChange = {
                      password = it
                  },
              )
              LogButton(
                  onClick = {
                      if(email.isEmpty() || password.isEmpty()){
                          Toast.makeText(context, "Please fill the both fields", Toast.LENGTH_LONG).show()
                      }else{
                          loginViewModel.login(email, password)
                      }
                  },
              )
              ClickAbleText(onClick = {
                  navController.navigate("signUp")
              })
          }
        }
    }

    loginResult?.let { result ->
        if(result.isSuccess){
            val user = result.getOrNull()
            user?.let {
                if(it.isAdmin) {
                    navController.navigate("dailyAdmin")
                } else {
                    navController.navigate("checkIn")
                }
                Log.d("CheckJoined", "${it.joinedMemberAt}")
            }
        }else{
            result.exceptionOrNull()?.message?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}