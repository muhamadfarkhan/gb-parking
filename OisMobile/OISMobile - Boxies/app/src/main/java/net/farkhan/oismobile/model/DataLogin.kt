package net.farkhan.oismobile.model

import com.google.gson.annotations.SerializedName

data class DataLogin(

	@field:SerializedName("GATEID")
	val gATEID: String? = null,

	@field:SerializedName("FOTO")
	val fOTO: String? = null,

	@field:SerializedName("VEHINIT")
	val vEHINIT: String? = null,

	@field:SerializedName("LUPDDTTIME")
	val lUPDDTTIME: String? = null,

	@field:SerializedName("USRNM")
	val uSRNM: String? = null,

	@field:SerializedName("FULLNM")
	val fULLNM: String? = null,

	@field:SerializedName("USRNMA")
	val uSRNMA: Any? = null,

	@field:SerializedName("FLAG")
	val fLAG: Any? = null,

	@field:SerializedName("INITNM")
	val iNITNM: String? = null,

	@field:SerializedName("PVEHTYPE")
	val pVEHTYPE: Any? = null,

	@field:SerializedName("STATUS")
	val sTATUS: String? = null,

	@field:SerializedName("PASSWORD")
	val pASSWORD: Any? = null,

	@field:SerializedName("LVLCD")
	val lVLCD: String? = null,

	@field:SerializedName("USRNME")
	val uSRNME: String? = null,

	@field:SerializedName("DEPTCD")
	val dEPTCD: String? = null,

	@field:SerializedName("WSID")
	val wSID: Any? = null
)