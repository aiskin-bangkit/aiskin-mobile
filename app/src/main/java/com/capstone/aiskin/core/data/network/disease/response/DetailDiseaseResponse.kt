package com.capstone.aiskin.core.data.network.disease.response

import com.google.gson.annotations.SerializedName

data class DetailDiseaseResponse(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("content")
    val content: List<String>? = null,

    @field:SerializedName("medicine_recommendation")
    val medicineRecommendation: List<String>? = null,

    @field:SerializedName("treatment_recommendation")
    val treatmentRecommendation: List<String>? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("created_at")
    val createdAt: Long? = null, // Timestamp sebagai Long

    @field:SerializedName("updated_at")
    val updatedAt: Long? = null  // Timestamp sebagai Long
)

data class CreatedAt(

    @field:SerializedName("seconds")
    val seconds: Long? = null,

    @field:SerializedName("nanoseconds")
    val nanoseconds: Int? = null
)
