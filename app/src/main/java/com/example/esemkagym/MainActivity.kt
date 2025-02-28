package com.example.esemkagym

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.esemkagym.dataclass.NavItem
import com.example.esemkagym.navigation.NavBody
import com.example.esemkagym.navigation.NavBot
import com.example.esemkagym.navigation.NavHeader
import com.example.esemkagym.navigation.Navigation
import com.example.esemkagym.navigation.isDrawerEnabled
import com.example.esemkagym.sharedperferences.SharedPreference
import com.example.esemkagym.ui.theme.EsemkaGymTheme
import com.example.esemkagym.viewmodel.AdminViewModel
import com.example.esemkagym.viewmodel.AttendanceViewModel
import com.example.esemkagym.viewmodel.ManageViewModel
import com.example.esemkagym.viewmodel.ReportViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val items = listOf(
                NavItem(
                    title = "Daily CheckIn Code",
                    route = "dailyAdmin",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                ),
                NavItem(
                    title = "Manage Members",
                    route = "manageMember",
                    selectedIcon = Icons.Filled.AccountBox,
                    unselectedIcon = Icons.Outlined.AccountBox,
                ),
                NavItem(
                    title = "Report",
                    route = "report",
                    selectedIcon = Icons.Filled.DateRange,
                    unselectedIcon = Icons.Outlined.DateRange,
                )
            )

            val context = this
            val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val adminViewModel = AdminViewModel(context)
            val manageViewModel = ManageViewModel(context)
            val attendanceViewModel = AttendanceViewModel(context)
            val reportViewModel = ReportViewModel(sharedPreferences)
            val tokenManager = SharedPreference(context)

            val isDrawerEnabled = isDrawerEnabled(navController)

            if (isDrawerEnabled) {
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            NavHeader(onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                            })
                            Spacer(modifier = Modifier.height(8.dp))
                            NavBody(
                                items = items
                            ) { currentNavigationItem ->
                                navController.navigate(currentNavigationItem.route)
                            }
                            NavBot(onClick = {
                                tokenManager.clearToken()
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            })
                        }
                    },
                    drawerState = drawerState
                ) {
                    Navigation(
                        navController = navController,
                        adminViewModel = adminViewModel,
                        manageViewModel = manageViewModel,
                        reportViewModel = reportViewModel,
                        attendanceViewModel = attendanceViewModel
                    )
                }
            } else {
                Navigation(
                    navController = navController,
                    adminViewModel = adminViewModel,
                    manageViewModel = manageViewModel,
                    reportViewModel = reportViewModel,
                    attendanceViewModel = attendanceViewModel
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EsemkaGymTheme {
        Greeting("Android")
    }
}