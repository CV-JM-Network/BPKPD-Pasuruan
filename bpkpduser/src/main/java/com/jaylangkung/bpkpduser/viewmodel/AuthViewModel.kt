package com.jaylangkung.bpkpduser.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaylangkung.bpkpduser.BuildConfig
import com.jaylangkung.bpkpduser.model.LoginRequest
import com.jaylangkung.bpkpduser.model.RegisterRequest
import com.jaylangkung.bpkpduser.repository.BaseRepositoryImpl
import com.jaylangkung.bpkpduser.utils.Constants
import com.jaylangkung.bpkpduser.utils.MySharedPreferences

class AuthViewModel(application: Application) : ViewModel() {

    private val appContext = application
    private val repository = BaseRepositoryImpl()
    private var myPreferences = MySharedPreferences(appContext)
    private var registerRequest: RegisterRequest? = null
    private var loginRequest: LoginRequest? = null

    val startActivityEvent = MutableLiveData<Pair<String, String>>()
    val register = "Regis"
    val confirmed = "Confirmed"
    val login = "Login"

    fun setRegisterRequest(registerRequest: RegisterRequest) {
        this.registerRequest = registerRequest
    }

    fun setLoginRequest(loginRequest: LoginRequest) {
        this.loginRequest = loginRequest
    }

    fun validate(): String {
        val errors = mutableListOf<String>()

        registerRequest?.let {
            if (it.email.isEmpty()) errors.add("Email")
            if (it.password.isEmpty()) errors.add("Kata sandi")
            if (it.nama.isEmpty()) errors.add("Nama")
            if (it.alamat.isEmpty()) errors.add("Alamat")
            if (it.telpon.isEmpty()) errors.add("Nomor")
        }

        loginRequest?.let {
            if (it.email.isEmpty()) errors.add("Email")
            if (it.password.isEmpty()) errors.add("Kata sandi")
        }

        val errMsg = errors.joinToString(", ") + " tidak boleh kosong"
        registerRequest = null
        loginRequest = null
        return if (errors.isEmpty()) "" else errMsg
    }

    fun register() {
        val tokenAuth = BuildConfig.API_KEY
        myPreferences.setValue(Constants.TOKEN_AUTH, tokenAuth)
        val registerResponse = repository.register(appContext, registerRequest!!, tokenAuth)

        registerResponse.observeForever { response ->
            if (response.status.contains("Success")) {
                startActivityEvent.value = Pair(register, "Registered")
            } else {
                startActivityEvent.value = Pair(register, response.status)
            }
        }
    }

    fun confirmRegister(kode: String) {
        val tokenAuth = BuildConfig.API_KEY
        myPreferences.setValue(Constants.TOKEN_AUTH, tokenAuth)
        val confirmRegisterResponse = repository.confirmRegister(appContext, kode, tokenAuth)

        confirmRegisterResponse.observeForever { response ->
            if (response.status.contains("Success")) {
                startActivityEvent.value = Pair(confirmed, "Confirmed")
            } else {
                startActivityEvent.value = Pair(confirmed, response.status)
            }
        }
    }

    fun login() {
        val tokenAuth = BuildConfig.API_KEY
        myPreferences.setValue(Constants.TOKEN_AUTH, tokenAuth)
        val loginResponse = repository.login(appContext, loginRequest!!, tokenAuth)

        loginResponse.observeForever { response ->
            if (response.data != null) {
                val data = response.data
                myPreferences.setValue(Constants.USER, Constants.LOGIN)
                myPreferences.setValueInteger(Constants.USER_ID, data.iduser)
                myPreferences.setValue(Constants.USER_EMAIL, data.email)
                myPreferences.setValue(Constants.USER_NAMA, data.nama)
                myPreferences.setValue(Constants.USER_ALAMAT, data.alamat)
                myPreferences.setValue(Constants.USER_TELP, data.telpon)
                myPreferences.setValue(Constants.USER_FOTO, data.img)
                startActivityEvent.value = Pair(login, Constants.LOGIN)
            } else {
                startActivityEvent.value = Pair(login, response.message)
            }
        }
    }
}