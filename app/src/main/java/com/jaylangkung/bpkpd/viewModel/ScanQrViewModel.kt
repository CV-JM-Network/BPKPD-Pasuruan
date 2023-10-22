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
import com.jaylangkung.bpkpd.dataClass.TerimaBerkasResponse
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
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<BerkasResponse>, response: Response<BerkasResponse>) {
                var errMsg = ""
                when (response.code()) {
                    200 -> {
                        for (i in response.body()!!.data.indices) {
                            response.body()!!.data[i].tanggal = convertDate(response.body()!!.data[i].tanggal)
                            response.body()!!.data[i].tanggalSelesai = convertDate(response.body()!!.data[i].tanggalSelesai)
                            response.body()!!.data[i].createddate = convertDate(response.body()!!.data[i].createddate)
                            response.body()!!.data[i].namaWp = convertCamelCase(response.body()!!.data[i].namaWp)
                            response.body()!!.data[i].desaKel = convertCamelCase(response.body()!!.data[i].desaKel)
                            response.body()!!.data[i].kecamatan = convertCamelCase(response.body()!!.data[i].kecamatan)
                            response.body()!!.data[i].contactPerson = if (response.body()!!.data[i].contactPerson == "0000000000000") "-" else response.body()!!.data[i].contactPerson
                        }
                        berkasData.postValue(response.body())
                    }

                    else -> {
                        errMsg = ErrorHandler().parseError(response.errorBody()!!.string())
                    }
                }

                if (errMsg.isNotEmpty()) {
                    ErrorHandler().responseHandler(appContext, "getBerkas | onResponse", errMsg)
                }
            }

            override fun onFailure(call: Call<BerkasResponse>, t: Throwable) {
                ErrorHandler().responseHandler(appContext, "getBerkas | onFailure", t.message.toString())
            }
        })
    }

    fun getRiwayatBerkas(idAdmin: String, url: String, tokenAuth: String) {
        RetrofitClient.apiService.getRiwayatBerkas(idAdmin, url, "", tokenAuth).enqueue(object : Callback<BerkasRiwayatResponse> {
            override fun onResponse(call: Call<BerkasRiwayatResponse>, response: Response<BerkasRiwayatResponse>) {
                var errMsg = ""
                when (response.code()) {
                    200 -> {
                        berkasRiwayatData.postValue(response.body())
                    }

                    else -> {
                        errMsg = ErrorHandler().parseError(response.errorBody()!!.string())
                    }
                }

                if (errMsg.isNotEmpty()) {
                    ErrorHandler().responseHandler(appContext, "getRiwayatBerkas | onResponse", errMsg)
                }
            }

            override fun onFailure(call: Call<BerkasRiwayatResponse>, t: Throwable) {
                ErrorHandler().responseHandler(appContext, "getRiwayatBerkas | onFailure", t.message.toString())
            }
        })
    }

    fun terimaBerkas(idAdmin: String, url: String, tokenAuth: String, callback: (Boolean) -> Unit) {
        RetrofitClient.apiService.terimaBerkas(idAdmin, url, "", tokenAuth).enqueue(object : Callback<TerimaBerkasResponse> {
            override fun onResponse(call: Call<TerimaBerkasResponse>, response: Response<TerimaBerkasResponse>) {
                when (response.code()) {
                    200 -> {
                        Toasty.success(appContext, response.body()!!.message, Toasty.LENGTH_LONG).show()
                        callback(true)
                    }

                    400 -> {
                        val msg = ErrorHandler().parseError(response.errorBody()!!.string())
                        Toasty.error(appContext, msg, Toasty.LENGTH_SHORT).show()
                        callback(false)
                    }

                    500 -> {
                        Toasty.error(appContext, "Internal Server Error", Toasty.LENGTH_SHORT).show()
                        callback(false)
                    }

                    else -> {
                        val msg = ErrorHandler().parseError(response.errorBody()!!.string())
                        ErrorHandler().responseHandler(appContext, "terimaBerkas | onResponse", msg)
                        callback(false)
                    }
                }
            }

            override fun onFailure(call: Call<TerimaBerkasResponse>, t: Throwable) {
                ErrorHandler().responseHandler(appContext, "terimaBerkas | onFailure", t.message.toString())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDate(date: String): String {
        if (date == "0000-00-00" || date == "0000-00-00 00:00:00" || date == "") return "-"
        val formatter = SimpleDateFormat("yyyy-MM-dd", appContext.resources.configuration.locales.get(0))
        val finalDate = formatter.parse(date)
        val newFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", appContext.resources.configuration.locales.get(0))
        return newFormatter.format(LocalDate.parse(finalDate?.let { formatter.format(it) }))
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