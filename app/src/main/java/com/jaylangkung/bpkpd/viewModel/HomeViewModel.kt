package com.jaylangkung.bpkpd.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaylangkung.bpkpd.dataClass.BerkasResponse
import com.jaylangkung.bpkpd.dataClass.BerkasRiwayatResponse
import com.jaylangkung.bpkpd.dataClass.UserData
import com.jaylangkung.bpkpd.retrofit.RetrofitClient
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.ErrorHandler
import com.jaylangkung.bpkpd.utils.MySharedPreferences

class HomeViewModel(application: Application) : ViewModel() {

    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences

    val berkasData: MutableLiveData<BerkasResponse> = MutableLiveData()
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
                        berkasData.postValue(response.body())
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
}