package com.capstone.aiskin.core.data.network.account.response

import com.google.gson.annotations.SerializedName

data class AccountResponse(

	@field:SerializedName("data")
	val data: AccountData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class AccountData(

	@field:SerializedName("image_profile")
	val imageProfile: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
