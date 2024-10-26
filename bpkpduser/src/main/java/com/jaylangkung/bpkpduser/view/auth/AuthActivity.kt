package com.jaylangkung.bpkpduser.view.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jaylangkung.bpkpduser.R
import com.jaylangkung.bpkpduser.databinding.ActivityAuthBinding
import com.jaylangkung.bpkpduser.utils.Utils
import com.jaylangkung.bpkpduser.viewmodel.AuthViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel

    companion object {
        const val DESTINATION = "destination"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this@AuthActivity, factory)[AuthViewModel::class.java]

        viewModel.init()
        if (intent.hasExtra(DESTINATION)) {
            when (intent.getStringExtra(DESTINATION)) {
                "login" -> {
                    Utils.loadFragment(supportFragmentManager, LoginFragment(), R.id.auth_fragment_container)
                }

                "register" -> {
                    Utils.loadFragment(supportFragmentManager, RegisterFragment(), R.id.auth_fragment_container)
                }

                "otp" -> {
                    Utils.loadFragment(supportFragmentManager, OtpFragment(), R.id.auth_fragment_container)
                }
            }
        } else {
            Utils.loadFragment(supportFragmentManager, LoginFragment(), R.id.auth_fragment_container)
        }
    }
}