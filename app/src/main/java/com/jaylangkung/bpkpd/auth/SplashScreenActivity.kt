package com.jaylangkung.bpkpd.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.bpkpd.BuildConfig
import com.jaylangkung.bpkpd.MainActivity
import com.jaylangkung.bpkpd.R
import com.jaylangkung.bpkpd.databinding.ActivitySplashScreenBinding
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.MySharedPreferences

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@SplashScreenActivity)

        val apiKey = BuildConfig.API_KEY
        myPreferences.setValue(Constants.TokenAuth, apiKey)

        binding.appVersion.text = getString(R.string.app_ver, BuildConfig.VERSION_NAME)

        Handler(Looper.getMainLooper()).postDelayed({
            //Ketika user sudah login tidak perlu ke halaman login lagi
            if (myPreferences.getValue(Constants.USER).equals(Constants.LOGIN)) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            }
        }, 500L)
    }
}