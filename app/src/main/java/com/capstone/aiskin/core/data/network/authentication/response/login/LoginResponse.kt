package com.capstone.aiskin.core.data.network.authentication.response.login

data class LoginResponse(
    val error: Boolean,
    val message: String?,
    val token: String?
)