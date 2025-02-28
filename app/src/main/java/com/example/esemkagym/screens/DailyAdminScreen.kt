package com.example.esemkagym.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.esemkagym.R
import com.example.esemkagym.components.Body
import com.example.esemkagym.components.Header
import com.example.esemkagym.viewmodel.AdminViewModel

@Composable
fun DailyAdminScreen(adminViewModel: AdminViewModel){
    val code by adminViewModel.codeResult.observeAsState("")

    LaunchedEffect(Unit) {
        adminViewModel.fetchCodeFromApi()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White,
    ) {
        Column(Modifier.fillMaxWidth()) {
            Header()
            Box(modifier = Modifier.fillMaxSize(),
                Alignment.Center){
                Body(
                    code = if (code.isNotEmpty()) code else "null"
                )
            }
        }

    }
}
