package com.jaylangkung.bpkpd.viewModel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.jaylangkung.bpkpd.dataClass.LoginWebappResponse
import com.jaylangkung.bpkpd.retrofit.RetrofitClient
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.ErrorHandler
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class ScanQrViewModel(application: Application) : ViewModel() {

    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences

    fun validateQRCode(result: String, callback: (String) -> Unit) {
        myPreferences = MySharedPreferences(appContext)

        if (result.contains("webapp")) {
            val idAdmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
            val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
            vibrate(appContext)
            loginWebApp(appContext, idAdmin, result, tokenAuth) { isSuccess ->
                if (isSuccess) {
                    callback("webapp_success")
                } else {
                    callback("webapp_failure")
                }
            }
        } else {
            val regex = Pattern.compile("(https:\\/\\/bkd\\.jaylangkung\\.co\\.id\\/get_info_surat\\.php\\?nomor_berkas=)(?:[DSKPN]{1,2}-\\d{2}\\.\\d{4}|\\d*)")
            val matcher = regex.matcher(result)
            if (matcher.find()) {
                vibrate(appContext)
                callback(result)
            } else {
                // Handle other cases if needed
                callback("invalid_qr_code")
            }
        }
    }

    private fun vibrate(ctx: Context) {
        val vibrator = ContextCompat.getSystemService(ctx, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION") vibrator.vibrate(200)
        }
    }

    private fun loginWebApp(ctx: Context, idAdmin: String, deviceId: String, tokenAuth: String, callback: (Boolean) -> Unit) {

        RetrofitClient.apiService.loginWebapp(idAdmin, deviceId, tokenAuth).enqueue(object : Callback<LoginWebappResponse> {
            override fun onResponse(call: Call<LoginWebappResponse>, response: Response<LoginWebappResponse>) {
                when (response.code()) {
                    200 -> {
                        Toasty.success(ctx, "Berhasil login ke webapp", Toasty.LENGTH_LONG).show()
                        callback(true)
                    }

                    400 -> {
                        val msg = ErrorHandler().parseError(response.errorBody()!!.string())
                        Toasty.error(ctx, msg, Toasty.LENGTH_SHORT).show()
                        callback(false)
                    }

                    500 -> {
                        Toasty.error(ctx, "Internal Server Error", Toasty.LENGTH_SHORT).show()
                        callback(false)
                    }

                    else -> {
                        val msg = ErrorHandler().parseError(response.errorBody()!!.string())
                        Toasty.error(ctx, msg, Toasty.LENGTH_SHORT).show()
                        callback(false)
                        ErrorHandler().responseHandler(ctx, "loginWebApp | onResponse", response.code().toString())
                    }
                }
            }

            override fun onFailure(call: Call<LoginWebappResponse>, t: Throwable) {
                ErrorHandler().responseHandler(ctx, "loginWebApp | onFailure", t.message.toString())
                callback(false)
            }
        })

    }
}