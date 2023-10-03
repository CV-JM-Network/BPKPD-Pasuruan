package com.jaylangkung.bpkpd.menu.scan

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.bpkpd.MainActivity
import com.jaylangkung.bpkpd.databinding.ActivityScanQrDetailBinding
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.MySharedPreferences

class ScanQrDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQrDetailBinding
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val EXTRA_RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@ScanQrDetailActivity)

        val idadmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(
                    Intent(this@ScanQrDetailActivity, MainActivity::class.java)
                        .putExtra(MainActivity.EXTRA_FRAGMENT, "scan")
                )
                finish()
            }
        })

    }
}