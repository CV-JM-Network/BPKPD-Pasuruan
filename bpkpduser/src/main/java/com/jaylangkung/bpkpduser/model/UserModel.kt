package com.jaylangkung.bpkpduser.model


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("data") val data: UserData?,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("tokenAuth") val tokenAuth: String
)

data class RegisterRequest(
    @SerializedName("alamat") val alamat: String,
    @SerializedName("email") val email: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("password") val password: String,
    @SerializedName("telpon") val telpon: String
)

data class UserData(
    @SerializedName("alamat") val alamat: String,
    @SerializedName("device_token") val deviceToken: String,
    @SerializedName("email") val email: String,
    @SerializedName("iduser") val iduser: Int,
    @SerializedName("img") val img: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("telpon") val telpon: String
)
