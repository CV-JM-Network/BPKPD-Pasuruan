package com.jaylangkung.bpkpd.retrofit

import com.jaylangkung.bpkpd.dataClass.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String,
    ): Call<LoginResponse>
}