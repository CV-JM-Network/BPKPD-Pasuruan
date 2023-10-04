package com.jaylangkung.bpkpd.dataClass

import com.google.gson.annotations.SerializedName

data class LoginWebappResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String,
    @SerializedName("device_token") val deviceToken: Any?,
    @SerializedName("idamin") val idamin: String
)