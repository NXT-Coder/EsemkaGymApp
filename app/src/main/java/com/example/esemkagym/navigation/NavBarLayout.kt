package com.example.esemkagym.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.esemkagym.R
import com.example.esemkagym.dataclass.NavItem

@Composable
fun isDrawerEnabled(navController: NavHostController): Boolean {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    return currentRoute in listOf("dailyAdmin", "manageMember", "report")
}

@Composable
fun NavHeader(onClick: () -> Unit){
    Box(modifier = Modifier.width(250.dp)){
        Image(
            modifier = Modifier
                .size(60.dp)
                .padding(15.dp)
                .clickable(onClick = {
                    onClick()
                }),
            painter = painterResource(id = R.drawable.baseline_clear_24),
            contentDescription = "clear"
        )

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.admin),
                contentDescription = "LogoAdmin",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 10.dp)
            )
            Text(
                text = "Admin",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "Admin@company.com",
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                fontStyle = FontStyle.Normal,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun NavBody(
    items: List<NavItem>,
    onClick: (NavItem) -> Unit
){
    var currentRoute by remember {
        mutableStateOf("dailyAdmin")
    }

    items.forEachIndexed { index, navItem ->
        NavigationDrawerItem(
            modifier = Modifier
                .width(250.dp)
                .clip(RoundedCornerShape(0.dp)),
            label = {
                Text(
                    text = navItem.title,
                    style = TextStyle(fontStyle = FontStyle.Normal, fontSize = 12.sp)
                )
            },
            selected = currentRoute == navItem.route,
            onClick = {
                currentRoute = navItem.route
                onClick(navItem)
            },
            icon = {
                Icon(
                    imageVector = if (currentRoute == navItem.route) {
                        navItem.selectedIcon
                    } else {
                        navItem.unselectedIcon
                    },
                    contentDescription = navItem.title
                )
            }
        )
    }
}

@Composable
fun NavBot(
    onClick: () -> Unit
){
    var currentRoute by remember {
        mutableStateOf("dailyAdmin")
    }

    NavigationDrawerItem(
        modifier = Modifier
            .width(250.dp)
            .clip(RectangleShape),
        label = {
            Text(
                text = "Logout",
                style = TextStyle(fontStyle = FontStyle.Normal, fontSize = 12.sp)
            )
        },
        selected = currentRoute == "login",
        onClick = {
            currentRoute = "login"
            onClick()
        },
        icon = {
            Icon(
                imageVector = if (currentRoute == "login") Icons.Filled.ExitToApp else Icons.Outlined.ExitToApp,
                contentDescription = "Logout"
            )
        }
    )
}