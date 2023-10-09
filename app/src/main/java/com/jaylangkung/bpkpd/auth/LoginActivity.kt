package com.jaylangkung.bpkpd.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.bpkpd.MainActivity
import com.jaylangkung.bpkpd.R
import com.jaylangkung.bpkpd.dataClass.LoginResponse
import com.jaylangkung.bpkpd.databinding.ActivityLoginBinding
import com.jaylangkung.bpkpd.retrofit.RetrofitClient
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.ErrorHandler
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var myPreferences: MySharedPreferences

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@LoginActivity)

        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        val deviceID = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        binding.apply {
            btnLogin.setOnClickListener {
                val email = tvValueEmailLogin.text.toString()
                val pass = tvValuePasswordLogin.text.toString()
                if (validate()) {
                    loginProcess(email, pass, deviceID, tokenAuth)
                    btnLogin.startAnimation()
                }
            }
        }
    }

    private fun validate(): Boolean {
        fun String.isValidEmail() = isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        when {
            binding.tvValueEmailLogin.text.toString() == "" -> {
                binding.tvValueEmailLogin.error = getString(R.string.email_cant_empty)
                binding.tvValueEmailLogin.requestFocus()
                return false
            }

            !binding.tvValueEmailLogin.text.toString().isValidEmail() -> {
                binding.tvValueEmailLogin.error = getString(R.string.email_format_error)
                binding.tvValueEmailLogin.requestFocus()
                return false
            }

            binding.tvValuePasswordLogin.text.toString() == "" -> {
                binding.tvValuePasswordLogin.error = getString(R.string.password_cant_empty)
                binding.tvValuePasswordLogin.requestFocus()
                return false
            }

            else -> return true
        }
    }

    private fun loginProcess(email: String, password: String, deviceID: String, tokenAuth: String) {
        RetrofitClient.apiService.login(email, password, deviceID, tokenAuth).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                binding.btnLogin.endAnimation()
                var errMsg = ""
                when (response.code()) {
                    200 -> {
                        val data = response.body()!!.data
                        myPreferences.setValue(Constants.USER, Constants.LOGIN)
                        myPreferences.setValue(Constants.USER_IDADMIN, data.idadmin.toString())
                        myPreferences.setValue(Constants.USER_EMAIL, data.email)
                        myPreferences.setValue(Constants.USER_NAMA, data.nama)
                        myPreferences.setValue(Constants.USER_ALAMAT, data.alamat)
                        myPreferences.setValue(Constants.USER_TELP, data.telp)
                        myPreferences.setValue(Constants.USER_FOTO, data.img)
                        myPreferences.setValue(Constants.USER_IDLEVEL, data.idlevel.toString())
                        myPreferences.setValue(Constants.USER_JUDUL, data.judul)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }

                    500 -> {
                        errMsg = "Internal Server Error"
                    }

                    else -> {
                        errMsg = ErrorHandler().parseError(response.errorBody()!!.string())
                    }
                }

                if (errMsg.isNotEmpty()) {
                    Toasty.error(this@LoginActivity, errMsg, Toasty.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.btnLogin.endAnimation()
                ErrorHandler().responseHandler(this@LoginActivity, "loginProcess | onResponse", t.message.toString())
            }
        })
    }
}