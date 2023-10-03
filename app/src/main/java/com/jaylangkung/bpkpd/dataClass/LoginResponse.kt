package com.jaylangkung.bpkpd.dataClass


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: UserData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tokenAuth")
    val tokenAuth: String
)

data class UserData(
    @SerializedName("alamat")
    val alamat: String,
    @SerializedName("device_token")
    val deviceToken: Any?,
    @SerializedName("email")
    val email: String,
    @SerializedName("idadmin")
    val idadmin: Int,
    @SerializedName("idlevel")
    val idlevel: Int,
    @SerializedName("img")
    val img: String,
    @SerializedName("judul")
    val judul: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("telp")
    val telp: String
)