package com.jaylangkung.bpkpd.menu.scan

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jaylangkung.bpkpd.MainActivity
import com.jaylangkung.bpkpd.databinding.ActivityScanQrDetailBinding
import com.jaylangkung.bpkpd.utils.Constants
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import com.jaylangkung.bpkpd.viewModel.ScanQrViewModel
import com.jaylangkung.bpkpd.viewModel.ViewModelFactory

class ScanQrDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQrDetailBinding
    private lateinit var viewModel: ScanQrViewModel
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val EXTRA_RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory)[ScanQrViewModel::class.java]
        myPreferences = MySharedPreferences(this@ScanQrDetailActivity)

        val idadmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
        val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()
        val result = intent.getStringExtra(EXTRA_RESULT).toString()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(
                    Intent(this@ScanQrDetailActivity, MainActivity::class.java).putExtra(MainActivity.EXTRA_FRAGMENT, "scan")
                )
                finish()
            }
        })

        binding.apply {
            viewModel.apply {
                getBerkas(idadmin, result, tokenAuth)
                berkasData.observe(this@ScanQrDetailActivity) { berkas ->
                    if (berkas != null) {
                        val data = berkas.data[0]
                        tvNoSurat.text = data.noPly
                    }
                }
            }
        }

    }
}