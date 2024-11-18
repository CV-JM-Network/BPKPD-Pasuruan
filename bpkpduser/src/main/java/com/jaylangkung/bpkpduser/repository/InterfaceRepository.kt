package com.jaylangkung.bpkpduser.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.jaylangkung.bpkpduser.model.DefaultResponse
import com.jaylangkung.bpkpduser.model.LoginRequest
import com.jaylangkung.bpkpduser.model.LoginResponse
import com.jaylangkung.bpkpduser.model.LoginWebAppRequest
import com.jaylangkung.bpkpduser.model.RegisterRequest

interface BaseRepository {
    fun register(context: Context, registerRequest: RegisterRequest, tokenAuth: String): LiveData<DefaultResponse>
    fun confirmRegister(context: Context, kode: String, tokenAuth: String): LiveData<DefaultResponse>
    fun login(context: Context, loginRequest: LoginRequest, tokenAuth: String): LiveData<LoginResponse>
    fun loginWebApp(context: Context, loginWebAppRequest: LoginWebAppRequest, tokenAuth: String): LiveData<DefaultResponse>
    fun forgotPassword(context: Context, email: String, tokenAuth: String): LiveData<DefaultResponse>
    fun confirmForgotPassword(context: Context, kode: String, tokenAuth: String): LiveData<DefaultResponse>
    fun changePassword(context: Context, email: String, password: String, repeatPassword: String, tokenAuth: String): LiveData<DefaultResponse>
}
