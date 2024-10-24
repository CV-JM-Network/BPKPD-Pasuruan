package com.jaylangkung.bpkpduser.view.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jaylangkung.bpkpduser.R
import com.jaylangkung.bpkpduser.databinding.ActivityAuthBinding
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

        if (intent.hasExtra(DESTINATION)) {
            when (intent.getStringExtra(DESTINATION)) {
                "login" -> {
                    loadFragment(LoginFragment())
                }

                "register" -> {
                    loadFragment(RegisterFragment())
                }

                "otp" -> {
                    loadFragment(OtpFragment())
                }
            }
        } else {
            loadFragment(OtpFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.auth_fragment_container, fragment)
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}