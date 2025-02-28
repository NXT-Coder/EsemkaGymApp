package com.example.esemkagym.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.esemkagym.screens.CheckInMemberScreen
import com.example.esemkagym.screens.DailyAdminScreen
import com.example.esemkagym.screens.LoginScreen
import com.example.esemkagym.screens.ManageMemberScreen
import com.example.esemkagym.screens.RegisteredScreen
import com.example.esemkagym.screens.ReportsScreen
import com.example.esemkagym.screens.SignUpScreen
import com.example.esemkagym.viewmodel.AdminViewModel
import com.example.esemkagym.viewmodel.AttendanceViewModel
import com.example.esemkagym.viewmodel.LoginViewModel
import com.example.esemkagym.viewmodel.ManageViewModel
import com.example.esemkagym.viewmodel.ReportViewModel
import com.example.esemkagym.viewmodel.SignUpViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    adminViewModel: AdminViewModel,
    manageViewModel: ManageViewModel,
    reportViewModel: ReportViewModel,
    attendanceViewModel: AttendanceViewModel
){
    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(navController = navController)
        }

        composable("checkIn"){
            CheckInMemberScreen(navController = navController)
        }

        composable("manageMember"){
            ManageMemberScreen(manageViewModel = manageViewModel)
        }

        composable("dailyAdmin"){
            DailyAdminScreen(adminViewModel = adminViewModel)
        }

        composable("signUp"){
            SignUpScreen(
                navController = navController,
                onSignUpSuccess = {
                    navController.navigate("registered")
                },
            )
        }

        composable("registered"){
            RegisteredScreen()
        }

        composable("report"){
            ReportsScreen(reportViewModel = reportViewModel, attendanceViewModel = attendanceViewModel)
        }
    }
}