package com.jaylangkung.bpkpd

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.jaylangkung.bpkpd.databinding.ActivityMainBinding
import com.jaylangkung.bpkpd.menu.home.HomeFragment
import com.jaylangkung.bpkpd.menu.scan.ScanQrFragment
import com.jaylangkung.bpkpd.menu.setting.SettingFragment
import com.jaylangkung.bpkpd.utils.MySharedPreferences
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val EXTRA_FRAGMENT = "extra_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@MainActivity)
        askPermission()
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
//                val iduser = myPreferences.getValue(Constants.USER_IDAKTIVASI).toString()
//                addToken(iduser, token)
            } else {
                val exception = task.exception
                exception?.message?.let {
                    Log.e(ContentValues.TAG, "Error retrieving FCM registration token: $it")
                }
            }
        }

        binding.apply {
            if (intent.hasExtra(EXTRA_FRAGMENT)) {
                when (intent.getStringExtra(EXTRA_FRAGMENT)) {
                    "home" -> {
                        loadFragment(HomeFragment())
                        bottomBar.selectTabById(R.id.nav_home, true)
                    }

                    "scan" -> {
                        loadFragment(ScanQrFragment())
                        bottomBar.selectTabById(R.id.nav_scan_qr, true)
                    }

                    "setting" -> {
                        loadFragment(SettingFragment())
                        bottomBar.selectTabById(R.id.nav_settings, true)
                    }
                }
            } else {
                loadFragment(HomeFragment())
            }

            bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
                override fun onTabSelected(lastIndex: Int, lastTab: AnimatedBottomBar.Tab?, newIndex: Int, newTab: AnimatedBottomBar.Tab) {
                    when (newTab.id) {
                        R.id.nav_home -> loadFragment(HomeFragment())
                        R.id.nav_scan_qr -> loadFragment(ScanQrFragment())
                        R.id.nav_settings -> loadFragment(SettingFragment())
                    }
                }
            })
        }
    }

    fun loadFragment(fragment: Fragment, bundle: Bundle? = null) {
        supportFragmentManager.beginTransaction().apply {
            if (bundle != null) {
                bundle.getString("page")?.let {
                    when (it) {
                        "home" -> binding.bottomBar.selectTabById(R.id.nav_home, true)
                        "scan" -> binding.bottomBar.selectTabById(R.id.nav_scan_qr, true)
                        "setting" -> binding.bottomBar.selectTabById(R.id.nav_settings, true)
                        else -> binding.bottomBar.selectTabById(R.id.nav_home, true)
                    }
                }
                fragment.arguments = bundle
            }
            replace(R.id.fragment_container, fragment)
            commit()
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

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this@MainActivity, permissionsToRequest.toTypedArray(), 100
            )
        }
    }
}
