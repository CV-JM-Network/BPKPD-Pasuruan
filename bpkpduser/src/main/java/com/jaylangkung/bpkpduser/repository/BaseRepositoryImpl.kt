package com.jaylangkung.bpkpduser.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jaylangkung.bpkpduser.model.DefaultResponse
import com.jaylangkung.bpkpduser.model.LoginRequest
import com.jaylangkung.bpkpduser.model.LoginResponse
import com.jaylangkung.bpkpduser.model.RegisterRequest
import com.jaylangkung.bpkpduser.utils.CustomHandler
import retrofit2.Call
import retrofit2.Response

class BaseRepositoryImpl : BaseRepository {
    private val apiService = RetrofitClient.apiService

    override fun register(
        context: Context,
        registerRequest: RegisterRequest,
        tokenAuth: String
    ): LiveData<DefaultResponse> {
        val registerData = MutableLiveData<DefaultResponse>()

        RetrofitClient.apiService.register(
            registerRequest.email,
            registerRequest.password,
            registerRequest.nama,
            registerRequest.alamat,
            registerRequest.telpon,
            "null",
            tokenAuth,
        ).enqueue(object : retrofit2.Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    registerData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "Register|onResponse", response.message())
                    registerData.postValue(
                        DefaultResponse(response.message(), "error", response.code())
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "Register|onFailure", t.message.toString())
                registerData.postValue(
                    DefaultResponse(t.message.toString(), "error", 500)
                )
            }
        })

        return registerData
    }

    override fun login(
        context: Context,
        loginRequest: LoginRequest,
        tokenAuth: String
    ): LiveData<LoginResponse> {
        val loginData = MutableLiveData<LoginResponse>()

        RetrofitClient.apiService.login(
            loginRequest.email,
            loginRequest.password,
            "null",
            tokenAuth,
        ).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    loginData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "Login|onResponse", response.message())
                    loginData.postValue(
                        LoginResponse(null, response.message(), "error", "")
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "Login|onFailure", t.message.toString())
                loginData.postValue(
                    LoginResponse(null, t.message.toString(), "error", "")
                )
            }
        })

        return loginData
    }
}