package net.farkhan.oismobile.model

import com.google.gson.annotations.SerializedName

data class ResponseInsert(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("NOTRAN")
	val nOTRAN: String? = null
)