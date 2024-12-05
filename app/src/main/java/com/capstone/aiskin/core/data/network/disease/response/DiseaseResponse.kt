package com.capstone.aiskin.core.data.network.disease.response

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.JsonAdapter

data class DiseaseResponse(
	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("treatment_recommendation")
	@JsonAdapter(FlexibleListDeserializer::class)
	val treatmentRecommendation: List<String>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Long? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("medicine_recommendation")
	val medicineRecommendation: List<String>? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("content")
	val content: List<String>? = null,

	@field:SerializedName("image")
	val image: String? = null
)
