package com.example.esemkagym.dataclass

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val isAdmin: Boolean,
    val joinedMemberAt: String,
    val token: String
)