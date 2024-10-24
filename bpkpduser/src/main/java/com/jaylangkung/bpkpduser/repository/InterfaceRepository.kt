package com.jaylangkung.bpkpduser.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.jaylangkung.bpkpduser.model.DefaultResponse
import com.jaylangkung.bpkpduser.model.LoginRequest
import com.jaylangkung.bpkpduser.model.LoginResponse
import com.jaylangkung.bpkpduser.model.RegisterRequest

interface BaseRepository {
    fun register(context: Context, registerRequest: RegisterRequest, tokenAuth: String): LiveData<DefaultResponse>
    fun confirmRegister(context: Context, kode: String, tokenAuth: String): LiveData<DefaultResponse>
    fun login(context: Context, loginRequest: LoginRequest, tokenAuth: String): LiveData<LoginResponse>
}
