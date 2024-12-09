package com.capstone.aiskin.core.data.local.model

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)