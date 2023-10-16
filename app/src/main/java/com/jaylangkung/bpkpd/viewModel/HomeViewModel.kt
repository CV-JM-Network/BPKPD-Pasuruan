package com.jaylangkung.bpkpd.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaylangkung.bpkpd.dataClass.BerkasData
import com.jaylangkung.bpkpd.dataClass.BerkasResponse
import com.jaylangkung.bpkpd.dataClass.BerkasRiwayatResponse
import com.jaylangkung.bpkpd.dataClass.UserData
import com.jaylangkung.bpkpd.retrofit.RetrofitClient
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.ErrorHandler
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(application: Application) : ViewModel() {

    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences

    val berkasData: MutableLiveData<List<BerkasData>> = MutableLiveData()
    var totalData: Int = 0
    val berkasRiwayatData: MutableLiveData<BerkasRiwayatResponse> = MutableLiveData()

    val userData: LiveData<UserData> = run {
        myPreferences = MySharedPreferences(appContext)
        val liveData = MutableLiveData<UserData>()
        liveData.postValue(
            UserData(
                myPreferences.getValue(Constants.USER_ALAMAT).toString(),
                "",
                myPreferences.getValue(Constants.USER_EMAIL).toString(),
                myPreferences.getValue(Constants.USER_IDADMIN).toString().toInt(),
                myPreferences.getValue(Constants.USER_IDLEVEL).toString().toInt(),
                myPreferences.getValue(Constants.USER_FOTO).toString(),
                myPreferences.getValue(Constants.USER_JUDUL).toString(),
                myPreferences.getValue(Constants.USER_NAMA).toString(),
                myPreferences.getValue(Constants.USER_TELP).toString()
            )
        )
        liveData
    }

    fun getDataBerkas(tabel: String, limit: Int, page: Int) {
        myPreferences = MySharedPreferences(appContext)
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        var errMsg = ""
        RetrofitClient.apiService.getDataBerkas(tabel, limit, page, tokenAuth).enqueue(object : retrofit2.Callback<BerkasResponse> {
            override fun onResponse(call: retrofit2.Call<BerkasResponse>, response: retrofit2.Response<BerkasResponse>) {
                when (response.code()) {
                    200 -> {
                        for (i in response.body()?.data!!.indices) {
                            response.body()?.data!![i].tanggal = dateConverter(response.body()?.data!![i].tanggal)
                            response.body()?.data!![i].tanggalSelesai = dateConverter(response.body()?.data!![i].tanggalSelesai)
                            response.body()?.data!![i].createddate = dateConverter(response.body()?.data!![i].createddate)
                            response.body()!!.data[i].namaWp = convertCamelCase(response.body()!!.data[i].namaWp)
                            response.body()!!.data[i].pembeli = convertCamelCase(response.body()!!.data[i].pembeli)
                            response.body()!!.data[i].penjual = convertCamelCase(response.body()!!.data[i].penjual)
                            response.body()!!.data[i].desaKel = convertCamelCase(response.body()!!.data[i].desaKel)
                            response.body()!!.data[i].kecamatan = convertCamelCase(response.body()!!.data[i].kecamatan)
                            response.body()!!.data[i].contactPerson = if (response.body()!!.data[i].contactPerson == "0000000000000") "-" else response.body()!!.data[i].contactPerson
                            response.body()!!.data[i].hargaTransaksi = convertStringToDecimal(response.body()!!.data[i].hargaTransaksi)
                            response.body()!!.data[i].bphtb = convertStringToDecimal(response.body()!!.data[i].bphtb)
                            response.body()!!.data[i].pengurangan = convertStringToDecimal(response.body()!!.data[i].pengurangan)
                        }
                        totalData = response.body()?.totalData ?: 0
                        berkasData.postValue(response.body()?.data)
                    }

                    else -> errMsg = ErrorHandler().parseError(response.errorBody()!!.string())
                }

                if (errMsg.isNotEmpty()) {
                    ErrorHandler().responseHandler(appContext, "getDataBerkas | onResponse", errMsg)
                }
            }

            override fun onFailure(call: retrofit2.Call<BerkasResponse>, t: Throwable) {
                ErrorHandler().responseHandler(appContext, "getDataBerkas | onFailure", t.message.toString())
            }
        })
    }

    private fun dateConverter(date: String): String {
        if (date == "0000-00-00 00:00:00" || date == "0000-00-00" || date == "") return "-"
        val initialDate: SimpleDateFormat = if (date.length == 19) {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        }
        val finalDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return finalDate.format(initialDate.parse(date)!!)
    }

    private fun convertCamelCase(str: String): String {
        val words = str.split(" ").toMutableList()
        for (i in words.indices) {
            words[i] = words[i].lowercase(Locale.ROOT).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        }
        return words.joinToString(" ")
    }

    private fun convertStringToDecimal(str: String): String {
        if (str == "") return "0"
        val number = str.replace(".0", "").toDouble()
        val formatter = DecimalFormat("#,###")
        return formatter.format(number)
    }
}