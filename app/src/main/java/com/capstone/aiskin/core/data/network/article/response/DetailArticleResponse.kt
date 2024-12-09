package com.capstone.aiskin.core.data.network.article.response

import com.google.gson.annotations.SerializedName

data class DetailArticleResponse(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: UpdatedAt? = null,

	@field:SerializedName("resource")
	val resource: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("content")
	val content: String? = null
)
data class UpdatedAt(

	@field:SerializedName("seconds")
	val seconds: Long? = null,

	@field:SerializedName("nanoseconds")
	val nanoseconds: Int? = null
)

data class CreatedAt(

	@field:SerializedName("seconds")
	val seconds: Long? = null,

	@field:SerializedName("nanoseconds")
	val nanoseconds: Int? = null
)
