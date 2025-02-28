package com.example.esemkagym.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.esemkagym.viewmodel.CheckInViewModel

class CheckInModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CheckInViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CheckInViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}