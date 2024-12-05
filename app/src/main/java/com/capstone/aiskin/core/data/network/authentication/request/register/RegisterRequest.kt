package com.capstone.aiskin.core.data.network.authentication.request.register

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)