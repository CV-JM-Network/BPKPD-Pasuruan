package com.jaylangkung.bpkpduser.view.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.jaylangkung.bpkpduser.R
import com.jaylangkung.bpkpduser.databinding.ActivityAuthBinding
import com.jaylangkung.bpkpduser.utils.Utils
import com.jaylangkung.bpkpduser.viewmodel.AuthViewModel
import com.jaylangkung.bpkpduser.viewmodel.ViewModelFactory

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth_fragment_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val factory = ViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this@AuthActivity, factory)[AuthViewModel::class.java]
        viewModel.init()

        // load default fragment
        Utils.loadFragment(supportFragmentManager, LoginFragment(), R.id.auth_fragment_container)
    }
}