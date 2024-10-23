package com.jaylangkung.bpkpduser.model

import com.google.gson.annotations.SerializedName

data class DefaultResponse(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    val code: Int
)
