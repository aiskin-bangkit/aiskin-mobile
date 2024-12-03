package com.capstone.aiskin.core.data.network.response

import com.google.gson.annotations.SerializedName

data class DiseaseResponseItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("treatment_recommendation")
    val treatmentRecommendation: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: Long? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val createdAt: Long? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("medicine_recommendation")
    val medicineRecommendation: String? = null
)
