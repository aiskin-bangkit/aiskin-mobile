package com.capstone.aiskin.core.data.network.history.request

data class HistoryRequest(
    val disease_name: String,
    val percentage: String,
    val image: String,
    val created_at: String,
    val other_suggestion: String?,
    val user_id: String
)


