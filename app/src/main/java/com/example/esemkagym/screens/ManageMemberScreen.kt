package com.example.esemkagym.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.esemkagym.components.HeaderMember
import com.example.esemkagym.viewmodel.ManageViewModel

@Composable
fun ManageMemberScreen(manageViewModel: ManageViewModel){
    Surface(modifier = Modifier) {
        Box(modifier = Modifier.fillMaxSize()){
            Column {
                HeaderMember(manageViewModel)
            }
        }
    }
}
