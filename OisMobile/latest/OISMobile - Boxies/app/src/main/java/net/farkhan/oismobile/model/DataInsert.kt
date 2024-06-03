package net.farkhan.oismobile.model

import com.google.gson.annotations.SerializedName

data class DataInsert(

	@field:SerializedName("DATETIMEIN")
	val dATETIMEIN: String? = null,

	@field:SerializedName("IDTransaction")
	val iDTransaction: String? = null,

	@field:SerializedName("REGNO")
	val rEGNO: String? = null,

	@field:SerializedName("USRNME")
	val uSRNME: String? = null,

	@field:SerializedName("NOTRAN")
	val nOTRAN: String? = null,

	@field:SerializedName("NONOTA")
	val nONOTA: Int? = null,

	@field:SerializedName("VEHCLASS")
	val vEHCLASS: String? = null
)