package com.jaylangkung.bpkpd.dataClass


import com.google.gson.annotations.SerializedName

data class UpdateProfilResponse(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String
)