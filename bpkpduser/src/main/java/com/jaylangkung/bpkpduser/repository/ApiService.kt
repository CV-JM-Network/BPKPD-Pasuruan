package com.jaylangkung.bpkpduser.repository

import com.jaylangkung.bpkpduser.model.DefaultResponse
import com.jaylangkung.bpkpduser.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService : AuthService, UserService

interface AuthService {
    @FormUrlEncoded
    @POST("user/registrasi")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("telpon") telp: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("user/registrasi/konfirmasi")
    fun confirmRegister(
        @Field("kode_konfirmasi") kode: String,
        @Field("device_id") deviceToken: String,
        @Header("Authorization") authorization: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("user/lupa/password")
    fun forgotPassword(
        @Field("email") email: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("user/lupa/password/memasukkan/kode")
    fun forgotPasswordConfirm(
        @Field("kode_lupa_password") kode: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("user/ganti/password")
    fun changePassword(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("repeat_password") repeatPassword: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String
    ): Call<DefaultResponse>

//    @FormUrlEncoded
//    @POST("webapp")
//    fun loginWebapp(
//        @Field("idadmin") idadmin: String,
//        @Field("device_id") deviceId: String,
//        @Header("Authorization") authorization: String,
//    ): Call<LoginWebappResponse>
}

interface UserService {
    @FormUrlEncoded
    @POST("user/registrasi")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("telpon") telp: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("user/registrasi/konfirmasi")
    fun confirmRegister(
        @Field("kode_konfirmasi") kode: String,
        @Field("device_id") deviceToken: String,
        @Header("Authorization") authorization: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") deviceToken: String,
        @Header("Authorization") authorization: String,
    ): Call<LoginResponse>
}