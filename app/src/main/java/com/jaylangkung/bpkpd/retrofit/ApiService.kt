package com.jaylangkung.bpkpd.retrofit

import com.jaylangkung.bpkpd.dataClass.BerkasResponse
import com.jaylangkung.bpkpd.dataClass.LoginResponse
import com.jaylangkung.bpkpd.dataClass.RiwayatBerkasResponse
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

    @GET("cari/berkas/dengan/qrcode")
    fun getBerkas(
        @Query("idadmin") idadmin: String,
        @Query("url") url: String,
        @Header("Authorization") authorization: String,
    ): Call<BerkasResponse>

    @FormUrlEncoded
    @POST("cek/riwayat/berkas")
    fun cekRiwayatBerkas(
        @Field("idadmin") idadmin: String,
        @Field("url") url: String,
        @Field("posisi") posisi: String,
        @Header("Authorization") authorization: String,
    ): Call<RiwayatBerkasResponse>
}