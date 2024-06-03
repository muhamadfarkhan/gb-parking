package net.farkhan.oismobile.model

import com.google.gson.annotations.SerializedName

data class ResponseLogin(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("dataLogin")
	val dataLogin: DataLogin? = null,

	@field:SerializedName("company")
	val company: String? = null

)