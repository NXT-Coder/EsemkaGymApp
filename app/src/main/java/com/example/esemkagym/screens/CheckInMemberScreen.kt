package com.example.esemkagym.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.esemkagym.R
import com.example.esemkagym.components.Attendances
import com.example.esemkagym.components.CheckIn
import com.example.esemkagym.components.Checkout
import com.example.esemkagym.components.HeaderBar
import com.example.esemkagym.dataclass.Attendance
import com.example.esemkagym.viewmodel.CheckInViewModel
import com.example.esemkagym.viewmodel.LoginViewModel
import com.example.esemkagym.viewmodelfactory.CheckInModelFactory
import com.example.esemkagym.viewmodelfactory.LoginViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun CheckInMemberScreen(navController: NavHostController){
    val context = LocalContext.current
    val checkInViewModel: CheckInViewModel = viewModel(factory = CheckInModelFactory(context))
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context))
    var checkIn by rememberSaveable { mutableStateOf("") }
    var isCheckedIn by rememberSaveable { mutableStateOf(false) }
    var isCheckingOut by rememberSaveable { mutableStateOf(false) }
    // Coroutine scope to launch suspend functions
    val coroutineScope = rememberCoroutineScope()

    // Load check-in status from SharedPreferences or other storage
    LaunchedEffect(Unit) {
        isCheckedIn = checkInViewModel.isCheckedIn() // Implement this method in CheckInViewModel
        isCheckingOut = checkInViewModel.isCheckingOut() // Implement this method in CheckInViewModel
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            Column {
                HeaderBar(
                    onClick = {
                        loginViewModel.logout()
                        Toast.makeText(context, "Logout successful", Toast.LENGTH_LONG).show()
                        navController.navigate("login")
                    },
                )
                if (!isCheckedIn) {
                    CheckIn(
                        checkIn = checkIn,
                        onCheckChange = {
                            checkIn = it
                        },
                        onClick = {
                            coroutineScope.launch {
                                val result = checkInViewModel.performCheckIn(checkIn)
                                Log.d("Message", "$result")
                                Toast.makeText(context, "CheckIn Success", Toast.LENGTH_LONG).show()

                                // Update state to reflect successful check-in
                                isCheckedIn = true
                                isCheckingOut = true // Set to true to show checkout button
                                checkInViewModel.saveCheckInStatus(true) // Save status
                                checkInViewModel.fetchAttendance()
                            }
                        }
                    )
                } else if (isCheckingOut) {
                    Checkout(
                        onClick = {
                            coroutineScope.launch {
                                val result = checkInViewModel.performCheckout() // Ensure this method is implemented
                                Log.d("Message", "$result")
                                Toast.makeText(context, "Checkout Success", Toast.LENGTH_LONG).show()

                                // Reset state after successful checkout
                                isCheckedIn = false
                                isCheckingOut = false
                                checkInViewModel.saveCheckInStatus(false) // Save status
                                checkInViewModel.fetchAttendance()
                            }
                        }
                    )
                }
                Attendances(checkInViewModel = checkInViewModel)
            }
        }
    }
}