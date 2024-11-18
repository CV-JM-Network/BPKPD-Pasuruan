package com.jaylangkung.bpkpduser.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaylangkung.bpkpduser.model.UserData
import com.jaylangkung.bpkpduser.utils.Constants
import com.jaylangkung.bpkpduser.utils.MySharedPreferences

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
                myPreferences.getValueInteger(Constants.USER_ID),
                myPreferences.getValue(Constants.USER_FOTO).toString(),
                myPreferences.getValue(Constants.USER_NAMA).toString(),
                myPreferences.getValue(Constants.USER_TELP).toString()
            )
        )
        liveData
    }

//    fun updateProfile(
//        idadmin: RequestBody,
//        nama: RequestBody,
//        alamat: RequestBody,
//        telp: RequestBody,
//        foto: MultipartBody.Part? = null,
//        callback: (Boolean) -> Unit
//    ) {
//        myPreferences = MySharedPreferences(appContext)
//        val tokenAuth = myPreferences.getValue(Constants.TOKEN_AUTH).toString()
//        RetrofitClient.apiService.updateProfile(
//            idadmin, nama, alamat, telp, foto, tokenAuth
//        ).enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                var errMsg = ""
//                when (response.code()) {
//                    200 -> {
//                        val data = response.body()?.data
//                        if (data != null) {
//                            myPreferences.setValue(Constants.USER_ALAMAT, data.alamat)
//                            myPreferences.setValue(Constants.USER_EMAIL, data.email)
//                            myPreferences.setValue(Constants.USER_FOTO, data.img)
//                            myPreferences.setValue(Constants.USER_NAMA, data.nama)
//                            myPreferences.setValue(Constants.USER_TELP, data.telp)
//                            myPreferences.setValue(Constants.USER_LEVEL, data.level)
//                            myPreferences.setValueInteger(Constants.USER_IDLEVEL, data.idLevel)
//                            callback(true)
//                        }
//                    }
//
//                    401 -> {
//                        callback(false)
//                    }
//
//                    else -> {
//                        callback(false)
//                    }
//                }
//                if (errMsg.isNotEmpty()) {
//                    Toasty.error(appContext, errMsg, Toasty.LENGTH_SHORT, true).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toasty.error(appContext, t.message.toString(), Toasty.LENGTH_SHORT, true).show()
//                callback(false)
//            }
//        })
//    }

    fun logout() {
        myPreferences.clear()
    }
}