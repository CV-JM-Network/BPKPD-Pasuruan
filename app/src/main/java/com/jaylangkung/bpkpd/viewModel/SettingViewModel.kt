package com.jaylangkung.bpkpd.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaylangkung.bpkpd.dataClass.LoginResponse
import com.jaylangkung.bpkpd.dataClass.UserData
import com.jaylangkung.bpkpd.retrofit.RetrofitClient
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.ErrorHandler
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingViewModel(application: Application) : ViewModel() {

    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences
    var photoUri: Uri? = null

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

    fun updateProfile(
        idadmin: RequestBody, nama: RequestBody, alamat: RequestBody, telp: RequestBody, foto: MultipartBody.Part? = null, callback: (Boolean) -> Unit
    ) {
        myPreferences = MySharedPreferences(appContext)
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        RetrofitClient.apiService.updateProfile(
            idadmin, nama, alamat, telp, foto, tokenAuth
        ).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                var errMsg = ""
                when (response.code()) {
                    200 -> {
                        val data = response.body()?.data
                        if (data != null) {
                            myPreferences.setValue(Constants.USER_ALAMAT, data.alamat)
                            myPreferences.setValue(Constants.USER_EMAIL, data.email)
                            myPreferences.setValue(Constants.USER_FOTO, data.img)
                            myPreferences.setValue(Constants.USER_JUDUL, data.judul)
                            myPreferences.setValue(Constants.USER_NAMA, data.nama)
                            myPreferences.setValue(Constants.USER_TELP, data.telp)
                        }
                        callback(true)
                    }

                    else -> {
                        errMsg = ErrorHandler().parseError(response.errorBody()!!.string())
                    }
                }

                if (errMsg.isNotEmpty()) {
                    ErrorHandler().responseHandler(appContext, "updateProfile | onResponse", errMsg)
                    callback(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                ErrorHandler().responseHandler(appContext, "updateProfile | onFailure", t.message.toString())
                callback(false)
            }

        })

    }


    fun logout() {
        myPreferences.clear()
    }
}