package com.example.esemkagym.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
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
import com.example.esemkagym.components.ClickAbleSignIn
import com.example.esemkagym.components.EmailSignUp
import com.example.esemkagym.components.GenderRadio
import com.example.esemkagym.components.HeaderSignUp
import com.example.esemkagym.components.NameTextField
import com.example.esemkagym.components.PasswordSignUp
import com.example.esemkagym.components.SignUpButton
import com.example.esemkagym.viewmodel.SignUpViewModel
import com.example.esemkagym.viewmodelfactory.SignUpViewModelFactory

@Composable
fun SignUpScreen(
    navController: NavController,
    onSignUpSuccess: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: SignUpViewModel = viewModel(factory = SignUpViewModelFactory(context))

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                HeaderSignUp()
                EmailSignUp(
                    email = email,
                    onEmailChange = { email = it }
                )
                PasswordSignUp(
                    password = password,
                    onPasswordChange = { password = it }
                )
                NameTextField(
                    name = name,
                    onNameChange = { name = it }
                )
                GenderRadio(
                    selectedGender = gender,
                    onGenderSelected = { selectedGender ->
                        gender = selectedGender
                    }
                )
                SignUpButton(
                    onClick = {
                        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || gender.isEmpty()){
                            Toast.makeText(context, "Please fill the both of fields", Toast.LENGTH_LONG).show()
                        }else{
                            viewModel.signUp(email, password, name, gender)
                            onSignUpSuccess()
                        }
                    }
                )
                ClickAbleSignIn(
                    onClick = {
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}
