package net.farkhan.oismobile.model

import com.google.gson.annotations.SerializedName

data class ResponseCheck(


        @field:SerializedName("CPSPAY")
        val cPSPAY: String? = null,

        @field:SerializedName("CPSAMT")
        val cPSAMT: Int? = null,

        @field:SerializedName("msg")
        val msg: String? = null,

        @field:SerializedName("CPSFEE")
        val cPSFEE: Int? = null,

        @field:SerializedName("timerange")
        val timerange: String? = null,

        @field:SerializedName("FEE")
        val fEE: Int? = null,

        @field:SerializedName("USRNMA")
        val uSRNMA: String? = null,

        @field:SerializedName("pic")
        val pic: String? = null,

        @field:SerializedName("CPSDATETIME")
        val cPSDATETIME: String? = null,

        @field:SerializedName("result")
        val result: String? = null,

        @field:SerializedName("DATETIMEOUT")
        val dATETIMEOUT: String? = null,

        @field:SerializedName("company")
        val company: String? = null,

        @field:SerializedName("CPSLT")
        val cPSLT: String? = null,

        @field:SerializedName("VEHCLASS")
        val vEHCLASS: String? = null
)