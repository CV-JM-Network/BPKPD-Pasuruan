package com.jaylangkung.bpkpd.retrofit

import com.jaylangkung.bpkpd.dataClass.BerkasResponse
import com.jaylangkung.bpkpd.dataClass.BerkasRiwayatResponse
import com.jaylangkung.bpkpd.dataClass.LoginResponse
import com.jaylangkung.bpkpd.dataClass.LoginWebappResponse
import com.jaylangkung.bpkpd.dataClass.TerimaBerkasResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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
    fun getRiwayatBerkas(
        @Field("idadmin") idadmin: String,
        @Field("url") url: String,
        @Field("posisi") posisi: String,
        @Header("Authorization") authorization: String,
    ): Call<BerkasRiwayatResponse>

    @FormUrlEncoded
    @POST("webapp")
    fun loginWebapp(
        @Field("idadmin") idadmin: String,
        @Field("device_id") deviceId: String,
        @Header("Authorization") authorization: String,
    ): Call<LoginWebappResponse>

    @Multipart
    @POST("admin/update/profile")
    fun updateProfile(
        @Part("idadmin") idadmin: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("telp") telp: RequestBody,
        @Part foto: MultipartBody.Part? = null,
        @Header("Authorization") authorization: String,
    ): Call<LoginResponse>

    @GET("get/data/berkas")
    fun getDataBerkas(
        @Query("data_tabel") tabel: String,
        @Query("limit") limit: Int,
        @Query("current_page") page: Int,
        @Header("Authorization") authorization: String,
    ): Call<BerkasResponse>

    @FormUrlEncoded
    @POST("terima_berkas/create")
    fun terimaBerkas(
        @Field("idadmin") idadmin: String,
        @Field("url") url: String,
        @Field("posisi") posisi: String,
        @Header("Authorization") authorization: String,
    ): Call<TerimaBerkasResponse>
}