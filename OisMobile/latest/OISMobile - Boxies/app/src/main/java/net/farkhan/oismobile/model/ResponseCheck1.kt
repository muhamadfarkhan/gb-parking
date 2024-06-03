package net.farkhan.oismobile.model

import com.google.gson.annotations.SerializedName

data class ResponseCheck1(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("jeniskend")
	val jeniskend: String? = null,

	@field:SerializedName("timerange")
	val timerange: String? = null,

	@field:SerializedName("DATETIMEOUT")
	val dATETIMEOUT: String? = null,

	@field:SerializedName("datas")
	val datas: Datas? = null,

	@field:SerializedName("FEE")
	val fEE: Int? = null,

	@field:SerializedName("USRNMA")
	val uSRNMA: Any? = null,

	@field:SerializedName("TRANFEE")
	val tRANFEE: Int? = null,

	@field:SerializedName("company")
	val company: String? = null,

	@field:SerializedName("pic")
	val pic: String? = null
)