package com.jaylangkung.bpkpd.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaylangkung.bpkpd.dataClass.UserData
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.MySharedPreferences

class HomeViewModel(application: Application) : ViewModel() {

    private val appContext: Application = application
    private lateinit var myPreferences: MySharedPreferences

    val userData: LiveData<UserData> = run {
        myPreferences = MySharedPreferences(appContext)
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
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
                myPreferences.getValue(Constants.USER_TELP).toString(),
                tokenAuth
            )
        )
        liveData
    }
}