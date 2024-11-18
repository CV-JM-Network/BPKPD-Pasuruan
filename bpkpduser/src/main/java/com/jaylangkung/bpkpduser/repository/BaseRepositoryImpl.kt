package com.jaylangkung.bpkpduser.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jaylangkung.bpkpduser.model.DefaultResponse
import com.jaylangkung.bpkpduser.model.LoginRequest
import com.jaylangkung.bpkpduser.model.LoginResponse
import com.jaylangkung.bpkpduser.model.LoginWebAppRequest
import com.jaylangkung.bpkpduser.model.RegisterRequest
import com.jaylangkung.bpkpduser.utils.CustomHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaseRepositoryImpl : BaseRepository {
    private val apiService = RetrofitClient.apiService

    override fun register(context: Context, registerRequest: RegisterRequest, tokenAuth: String): LiveData<DefaultResponse> {
        val registerData = MutableLiveData<DefaultResponse>()

        apiService.register(
            registerRequest.email,
            registerRequest.password,
            registerRequest.nama,
            registerRequest.alamat,
            registerRequest.telpon,
            "null",
            tokenAuth,
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    registerData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "Register|onResponse", response.message())
                    val errResp = CustomHandler().parseError(response.errorBody()!!.string())
                    registerData.postValue(
                        DefaultResponse(response.message(), errResp.first)
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "Register|onFailure", t.message.toString())
                registerData.postValue(
                    DefaultResponse(t.message.toString(), "error")
                )
            }
        })

        return registerData
    }

    override fun confirmRegister(context: Context, kode: String, tokenAuth: String): LiveData<DefaultResponse> {
        val confirmData = MutableLiveData<DefaultResponse>()

        apiService.confirmRegister(
            kode,
            "null",
            tokenAuth,
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    confirmData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "ConfirmRegister|onResponse", response.message())
                    val errResp = CustomHandler().parseError(response.errorBody()!!.string())
                    confirmData.postValue(
                        DefaultResponse(response.message(), errResp.first)
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "ConfirmRegister|onFailure", t.message.toString())
                confirmData.postValue(
                    DefaultResponse(t.message.toString(), "error")
                )
            }
        })

        return confirmData
    }

    override fun login(context: Context, loginRequest: LoginRequest, tokenAuth: String): LiveData<LoginResponse> {
        val loginData = MutableLiveData<LoginResponse>()

        apiService.login(
            loginRequest.email,
            loginRequest.password,
            "null",
            tokenAuth,
        ).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    loginData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "Login|onResponse", response.message())
                    val errResp = CustomHandler().parseError(response.errorBody()!!.string())
                    loginData.postValue(
                        LoginResponse(null, response.message(), errResp.first, "")
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

    override fun loginWebApp(context: Context, loginWebAppRequest: LoginWebAppRequest, tokenAuth: String): LiveData<DefaultResponse> {
        val loginWebappData = MutableLiveData<DefaultResponse>()

        apiService.loginWebapp(
            loginWebAppRequest.idUser,
            loginWebAppRequest.qrString,
            tokenAuth,
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    loginWebappData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "LoginWebapp|onResponse", response.message())
                    val errResp = CustomHandler().parseError(response.errorBody()!!.string())
                    loginWebappData.postValue(
                        DefaultResponse(response.message(), errResp.first)
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "LoginWebapp|onFailure", t.message.toString())
                loginWebappData.postValue(
                    DefaultResponse(t.message.toString(), "error")
                )
            }
        })

        return loginWebappData
    }

    override fun forgotPassword(context: Context, email: String, tokenAuth: String): LiveData<DefaultResponse> {
        val forgotData = MutableLiveData<DefaultResponse>()

        apiService.forgotPassword(
            email,
            "null",
            tokenAuth,
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    forgotData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "ForgotPassword|onResponse", response.message())
                    val errResp = CustomHandler().parseError(response.errorBody()!!.string())
                    forgotData.postValue(
                        DefaultResponse(response.message(), errResp.first)
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "ForgotPassword|onFailure", t.message.toString())
                forgotData.postValue(
                    DefaultResponse(t.message.toString(), "error")
                )
            }
        })

        return forgotData
    }

    override fun confirmForgotPassword(context: Context, kode: String, tokenAuth: String): LiveData<DefaultResponse> {
        val confirmForgotData = MutableLiveData<DefaultResponse>()

        apiService.forgotPasswordConfirm(
            kode,
            "null",
            tokenAuth,
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    confirmForgotData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "ConfirmForgotPassword|onResponse", response.message())
                    val errResp = CustomHandler().parseError(response.errorBody()!!.string())
                    confirmForgotData.postValue(
                        DefaultResponse(response.message(), errResp.first)
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "ConfirmForgotPassword|onFailure", t.message.toString())
                confirmForgotData.postValue(
                    DefaultResponse(t.message.toString(), "error")
                )
            }
        })

        return confirmForgotData
    }

    override fun changePassword(context: Context, email: String, password: String, repeatPassword: String, tokenAuth: String): LiveData<DefaultResponse> {
        val changePasswordData = MutableLiveData<DefaultResponse>()

        apiService.changePassword(
            email,
            password,
            repeatPassword,
            "null",
            tokenAuth,
        ).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    changePasswordData.postValue(response.body())
                } else {
                    CustomHandler().responseHandler(context, "ChangePassword|onResponse", response.message())
                    val errResp = CustomHandler().parseError(response.errorBody()!!.string())
                    changePasswordData.postValue(
                        DefaultResponse(response.message(), errResp.first)
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                CustomHandler().responseHandler(context, "ChangePassword|onFailure", t.message.toString())
                changePasswordData.postValue(
                    DefaultResponse(t.message.toString(), "error")
                )
            }
        })

        return changePasswordData
    }
}