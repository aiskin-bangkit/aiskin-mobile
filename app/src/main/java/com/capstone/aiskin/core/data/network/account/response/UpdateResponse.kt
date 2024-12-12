package com.capstone.aiskin.core.data.network.account.response

data class UpdateResponse(
    val status: String,
    val message: String,
    val data: UpdateData
)

data class UpdateData(
    val id: String,
    val name: String,
    val email: String,
    val image_profile: String?,
    val token: String
)