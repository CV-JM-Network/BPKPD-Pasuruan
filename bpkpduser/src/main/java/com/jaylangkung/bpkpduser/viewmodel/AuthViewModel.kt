package com.jaylangkung.bpkpduser.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaylangkung.bpkpduser.BuildConfig
import com.jaylangkung.bpkpduser.model.LoginRequest
import com.jaylangkung.bpkpduser.model.LoginWebAppRequest
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
    private var loginWebAppRequest: LoginWebAppRequest? = null

    val startActivityEvent = MutableLiveData<Pair<String, String>>()
    var userEmail = "user_email"
    val register = "Regis"
    val confirmed = "Confirmed"
    val login = "Login"
    val webapp = "Webapp"
    val forgot = "Forgot"
    val change = "Change"

    fun init() {
        val tokenAuth = BuildConfig.API_KEY
        myPreferences.setValue(Constants.TOKEN_AUTH, tokenAuth)

        if (myPreferences.getValue(Constants.USER).toString() == Constants.LOGIN) {
            startActivityEvent.value = Pair(login, Constants.LOGIN)
        }
    }

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
        return if (errors.isEmpty()) "" else errMsg
    }

    fun validateQRCode(qrString: String): String {
        if (qrString.contains("webapp")) {
            val idUser = myPreferences.getValueInteger(Constants.USER_ID).toString()
            loginWebAppRequest = LoginWebAppRequest(idUser, qrString)
            return ""
        } else {
            return "qr_code_invalid"
        }
    }

    fun register() {
        val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
        val registerResponse = repository.register(appContext, registerRequest!!, tokenAuth)

        registerResponse.observeForever { response ->
            val status = if (response.status.contains("Success")) "Registered" else response.status
            startActivityEvent.value = Pair(register, status)
        }

        registerRequest = null
        loginRequest = null
    }

    fun confirmRegister(kode: String) {
        val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
        val confirmRegisterResponse = repository.confirmRegister(appContext, kode, tokenAuth)

        confirmRegisterResponse.observeForever { response ->
            val status = if (response.status.contains("Success")) "Confirmed" else response.status
            startActivityEvent.value = Pair(confirmed, status)
        }
    }

    fun login() {
        val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
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

        registerRequest = null
        loginRequest = null
    }

    fun loginWebApp() {
        val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
        val loginWebAppResponse = repository.loginWebApp(appContext, loginWebAppRequest!!, tokenAuth)

        loginWebAppResponse.observeForever { response ->
            val status = if (response.status.contains("Success")) "Webapp Success" else response.status
            startActivityEvent.value = Pair(webapp, status)
        }
    }

    fun forgotPassword(email: String) {
        val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
        val forgotPasswordResponse = repository.forgotPassword(appContext, email, tokenAuth)

        forgotPasswordResponse.observeForever { response ->
            val status = if (response.status.contains("Success")) "Reset Code Sent" else response.status
            startActivityEvent.value = Pair(forgot, status)
        }
    }

    fun confirmForgotPassword(kode: String) {
        val tokenAuth = BuildConfig.API_KEY
        myPreferences.setValue(Constants.TOKEN_AUTH, tokenAuth)
        val confirmForgotPasswordResponse = repository.confirmForgotPassword(appContext, kode, tokenAuth)

        confirmForgotPasswordResponse.observeForever { response ->
            val status = if (response.status.contains("Success")) "Change Password" else response.status
            startActivityEvent.value = Pair(confirmed, status)
        }
    }

    fun changePassword(email: String, password: String) {
        val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
        val changePasswordResponse = repository.changePassword(appContext, email, password, password, tokenAuth)

        changePasswordResponse.observeForever { response ->
            val status = if (response.status.contains("Success")) "Password Changed" else response.status
            startActivityEvent.value = Pair(change, status)
        }
    }

}