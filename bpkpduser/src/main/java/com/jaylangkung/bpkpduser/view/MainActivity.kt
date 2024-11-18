package com.jaylangkung.bpkpduser.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jaylangkung.bpkpduser.R
import com.jaylangkung.bpkpduser.databinding.ActivityMainBinding
import com.jaylangkung.bpkpduser.utils.MySharedPreferences
import com.jaylangkung.bpkpduser.utils.Utils.loadFragment
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val EXTRA_FRAGMENT = "extra_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@MainActivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        askPermission()

        binding.apply {
            if (intent.hasExtra(EXTRA_FRAGMENT)) {
                when (intent.getStringExtra(EXTRA_FRAGMENT)) {
                    "home" -> {
                        loadFragment(supportFragmentManager, HomeFragment(), R.id.main_fragment_container)
                        bottomBar.selectTabById(R.id.nav_home, true)
                    }

                    "scan" -> {
                        loadFragment(supportFragmentManager, ScanQrFragment(), R.id.main_fragment_container)
                        bottomBar.selectTabById(R.id.nav_scan_qr, true)
                    }

                    "setting" -> {
                        loadFragment(supportFragmentManager, SettingFragment(), R.id.main_fragment_container)
                        bottomBar.selectTabById(R.id.nav_settings, true)
                    }
                }
            } else {
                loadFragment(supportFragmentManager, HomeFragment(), R.id.main_fragment_container)
            }

            bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
                override fun onTabSelected(lastIndex: Int, lastTab: AnimatedBottomBar.Tab?, newIndex: Int, newTab: AnimatedBottomBar.Tab) {
                    when (newTab.id) {
                        R.id.nav_home -> loadFragment(supportFragmentManager, HomeFragment(), R.id.main_fragment_container)
                        R.id.nav_scan_qr -> loadFragment(supportFragmentManager, ScanQrFragment(), R.id.main_fragment_container)
                        R.id.nav_settings -> loadFragment(supportFragmentManager, SettingFragment(), R.id.main_fragment_container)
                    }
                }
            })
        }
    }

    private fun askPermission() {
        val cameraPermission = Manifest.permission.CAMERA
        val readStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writeStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissionsToRequest = mutableListOf<String>()

        // Check for notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Check for camera, storage, and location permissions
        if (ContextCompat.checkSelfPermission(this@MainActivity, cameraPermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(cameraPermission)
        }
        if (ContextCompat.checkSelfPermission(this@MainActivity, readStoragePermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(readStoragePermission)
        }
        if (ContextCompat.checkSelfPermission(this@MainActivity, writeStoragePermission) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(writeStoragePermission)
        }
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this@MainActivity, permissionsToRequest.toTypedArray(), 100
            )
        }
    }
}