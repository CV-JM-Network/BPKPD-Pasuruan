package com.jaylangkung.bpkpd.notification

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import com.jaylangkung.bpkpd.dataClass.LoginResponse
import com.jaylangkung.bpkpd.retrofit.RetrofitClient
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.ErrorHandler
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var myPreferences: MySharedPreferences

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification!!.title
        val body = remoteMessage.notification!!.body

        NotificationHelper(applicationContext).displayNotification(title!!, body!!)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        myPreferences = MySharedPreferences(this@MyFirebaseMessagingService)
        val idadmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        val newToken = Firebase.messaging.token.result.toString()
        updateDeviceToken(idadmin, newToken, tokenAuth)
        Log.d("TAG", "Refreshed token: $token")
    }

    private fun updateDeviceToken(idadmin: String, deviceToken: String, tokenAuth: String) {
        RetrofitClient.apiService.updateDeviceToken(idadmin, deviceToken, tokenAuth).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        Log.d("updateDeviceToken", response.body()!!.message)
                    }
                } else {
                    ErrorHandler().responseHandler(
                        this@MyFirebaseMessagingService, "updateDeviceToken | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                ErrorHandler().responseHandler(
                    this@MyFirebaseMessagingService, "updateDeviceToken | onFailure", t.message.toString()
                )
            }
        })
    }
}