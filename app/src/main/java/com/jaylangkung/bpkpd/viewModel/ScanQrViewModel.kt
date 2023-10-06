package com.jaylangkung.bpkpd.viewModel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaylangkung.bpkpd.dataClass.BerkasResponse
import com.jaylangkung.bpkpd.dataClass.BerkasRiwayatResponse
import com.jaylangkung.bpkpd.dataClass.LoginWebappResponse
import com.jaylangkung.bpkpd.retrofit.RetrofitClient
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.ErrorHandler
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.regex.Pattern

class ScanQrViewModel(application: Application) : ViewModel() {

    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences

    val berkasData: MutableLiveData<BerkasResponse> = MutableLiveData()
    val berkasRiwayatData: MutableLiveData<BerkasRiwayatResponse> = MutableLiveData()

    fun validateQRCode(result: String, callback: (String) -> Unit) {
        myPreferences = MySharedPreferences(appContext)

        val idAdmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        if (result.contains("webapp")) {
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
                callback("invalid_qr_code")
            }
        }
    }

    fun getBerkas(idAdmin: String, url: String, tokenAuth: String) {
        RetrofitClient.apiService.getBerkas(idAdmin, url, tokenAuth).enqueue(object : Callback<BerkasResponse> {
            override fun onResponse(call: Call<BerkasResponse>, response: Response<BerkasResponse>) {
                when (response.code()) {
                    200 -> {
                        for (i in response.body()!!.data.indices) {
                            response.body()!!.data[i].namaWp = convertCamelCase(response.body()!!.data[i].namaWp)
                            response.body()!!.data[i].desaKel = convertCamelCase(response.body()!!.data[i].desaKel)
                            response.body()!!.data[i].kecamatan = convertCamelCase(response.body()!!.data[i].kecamatan)
                        }
                        berkasData.postValue(response.body())
                    }

                    400 -> {
                        val error = ErrorHandler().parseError(response.errorBody()!!.string())
                        Toasty.error(appContext, error, Toasty.LENGTH_LONG).show()
                    }

                    else -> {
                        val error = ErrorHandler().parseError(response.errorBody()!!.string())
                        Toasty.error(appContext, error, Toasty.LENGTH_LONG).show()
                        ErrorHandler().responseHandler(appContext, "getBerkas | onResponse", response.code().toString())
                    }
                }
            }

            override fun onFailure(call: Call<BerkasResponse>, t: Throwable) {
                Toasty.error(appContext, t.message.toString(), Toasty.LENGTH_LONG).show()
                ErrorHandler().responseHandler(appContext, "getBerkas | onFailure", t.message.toString())
            }
        })
    }

    fun getRiwayatBerkas(idAdmin: String, url: String, tokenAuth: String) {
        RetrofitClient.apiService.getRiwayatBerkas(idAdmin, url, "", tokenAuth).enqueue(object : Callback<BerkasRiwayatResponse> {
            override fun onResponse(call: Call<BerkasRiwayatResponse>, response: Response<BerkasRiwayatResponse>) {
                when (response.code()) {
                    200 -> {
                        berkasRiwayatData.postValue(response.body())
                    }

                    400 -> {
                        val error = ErrorHandler().parseError(response.errorBody()!!.string())
                        Toasty.error(appContext, error, Toasty.LENGTH_LONG).show()
                    }

                    else -> {
                        val error = ErrorHandler().parseError(response.errorBody()!!.string())
                        Toasty.error(appContext, error, Toasty.LENGTH_LONG).show()
                        ErrorHandler().responseHandler(appContext, "getRiwayatBerkas | onResponse", response.code().toString())
                    }
                }
            }

            override fun onFailure(call: Call<BerkasRiwayatResponse>, t: Throwable) {
                Toasty.error(appContext, t.message.toString(), Toasty.LENGTH_LONG).show()
                ErrorHandler().responseHandler(appContext, "getRiwayatBerkas | onFailure", t.message.toString())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDate(rawDate: String): String {
        if (rawDate == "0000-00-00") return "-"
        val formatter = SimpleDateFormat("yyyy-MM-dd", appContext.resources.configuration.locales.get(0))
        val date = formatter.parse(rawDate)
        val newFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", appContext.resources.configuration.locales.get(0))
        return newFormatter.format(LocalDate.parse(date?.let { formatter.format(it) }))
    }

    private fun vibrate(ctx: Context) {
        val vibrator = ContextCompat.getSystemService(ctx, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION") vibrator.vibrate(200)
        }
    }

    private fun convertCamelCase(str: String): String {
        val words = str.split(" ").toMutableList()
        for (i in words.indices) {
            words[i] = words[i].lowercase(Locale.ROOT).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        }
        return words.joinToString(" ")
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